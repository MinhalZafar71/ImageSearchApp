package com.sftech.imagesearchapp.presentation.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.use_case.GetImageUseCase
import com.sftech.imagesearchapp.util.MainState
import com.sftech.imagesearchapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {


    val list: MutableState<MainState> = mutableStateOf(MainState())

    private var searchJob: Job? = null


    init {
        getImageList("")
    }

    fun getImageList(query: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {

            delay(500L)

            getImageUseCase.invoke(query).collect { it ->
                when (it) {
                    is Resource.Loading -> {
                        list.value = MainState(isLoading = true)
                    }

                    is Resource.Error -> {
                        list.value = MainState(error = it.message.toString())
                    }

                    is Resource.Success -> {
                        it.data?.let { item ->
                            list.value = MainState(data = item)
                        }
                    }
                }
            }
        }
    }

}