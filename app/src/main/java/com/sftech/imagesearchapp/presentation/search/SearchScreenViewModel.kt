package com.sftech.imagesearchapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.use_case.GetImageUseCase
import com.sftech.imagesearchapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    // Search query flow with debouncing
    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        getImageList()
        triggerInitialLoad()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getImageList() {

//        searchJob?.cancel()
//
//        searchJob = viewModelScope.launch {
//            // Only debounce if it's a user search (not initial load)
//            if (query.isNotBlank()) {
//                delay(500L)
//            }
//
//            getImageUseCase.invoke(query).collect { it ->
//                when (it) {
//                    is Resource.Loading -> {
//                        _list.value = list.value.copy(isLoading = true)
//                    }
//
//                    is Resource.Error -> {
//                        _list.value = list.value.copy(
//                            isLoading = false,
//                            error = it.message.toString()
//                        )
//                    }
//
//                    is Resource.Success -> {
//                        it.data?.let { item ->
//                            _list.value = list.value.copy(
//                                isLoading = false,
//                                data = item,
//                                error = ""
//                            )
//                        }
//                    }
//                }
//            }
//        }

        searchJob = viewModelScope.launch {
            @OptIn(FlowPreview::class) _searchQuery.onStart { emit("") }.distinctUntilChanged()
                .debounce(500.milliseconds).flatMapLatest { query ->
                    _state.value = State.Loading
                    getImageUseCase.invoke(query).map { resources ->
                        when (resources) {
                            is Resource.Success -> {
                                resources.data?.let { image ->
                                    if (image.isEmpty()) {
                                        State.Error("No images found")
                                    } else {
                                        State.Success(image)
                                    }
                                } ?: State.Error("Empty response")
                            }

                            is Resource.Error -> {
                                State.Error(resources.message ?: "Unknown Error")
                            }

                            is Resource.Loading -> {
                                State.Loading
                            }
                        }
                    }.catch { e ->
                        emit(State.Error(e.message ?: "Unknown error"))
                    }
                }.collect { newState ->
                    _state.value = newState
                }
        }


    }

    private fun triggerInitialLoad() {
        _searchQuery.value = "" // Triggers initial load
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun retry() {
        onSearchQueryChanged(_searchQuery.value)
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }



    // State definition
    sealed interface State {
        data object Idle : State
        data object Loading : State
        data class Success(val images: List<ImageItem>) : State
        data class Error(val message: String) : State
    }

}



