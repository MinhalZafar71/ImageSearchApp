package com.sftech.imagesearchapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.use_case.SearchImagesUseCase
import com.sftech.imagesearchapp.presentation.navigation.Route
import com.sftech.imagesearchapp.presentation.navigation.Route.previewImageWithId
import com.sftech.imagesearchapp.util.Resource
import com.sftech.imagesearchapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchImagesUseCase: SearchImagesUseCase
) : ViewModel() {


    private val _searchScreenState = MutableStateFlow<SearchScreenState>(SearchScreenState.Idle)
    val searchScreenState: StateFlow<SearchScreenState> = _searchScreenState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        getImageList()
        triggerInitialLoad()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getImageList() {

        searchJob = viewModelScope.launch {
            @OptIn(FlowPreview::class)
            _searchQuery
                .onStart { emit("") }
                .distinctUntilChanged()
                .debounce(500.milliseconds)
                .flatMapLatest { query ->
                    _searchScreenState.value = SearchScreenState.Loading
                    searchImagesUseCase.invoke(query).map { resources ->
                        when (resources) {
                            is Resource.Success -> {
                                resources.data?.let { image ->
                                    if (image.isEmpty()) {
                                        SearchScreenState.Error("No images found")
                                    } else {
                                        SearchScreenState.Success(image)
                                    }
                                } ?: SearchScreenState.Error("Empty response")
                            }

                            is Resource.Error -> {
                                SearchScreenState.Error(resources.message ?: "Unknown Error")
                            }

                            is Resource.Loading -> {
                                SearchScreenState.Loading
                            }
                        }
                    }.catch { e ->
                        emit(SearchScreenState.Error(e.message ?: "Unknown error"))
                    }
                }
                .collect { newState ->
                    _searchScreenState.value = newState
                }
        }


    }



    private fun triggerInitialLoad() {
        _searchQuery.value = ""
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

    fun onPreviewImageClick(imageId: String){
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate(previewImageWithId(imageId)))
        }
    }


    // State definition
    sealed interface SearchScreenState {
        data object Idle : SearchScreenState
        data object Loading : SearchScreenState
        data class Success(val images: List<ImageItem>) : SearchScreenState
        data class Error(val message: String) : SearchScreenState
    }

}



