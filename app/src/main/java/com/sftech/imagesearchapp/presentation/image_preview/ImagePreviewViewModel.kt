package com.sftech.imagesearchapp.presentation.image_preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.use_case.SearchSingleImageUseCase
import com.sftech.imagesearchapp.util.Resource
import com.sftech.imagesearchapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImagePreviewViewModel @Inject constructor(
    private val searchSingleImageUseCase: SearchSingleImageUseCase
) : ViewModel() {


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun loadImageDetails(imageId: String) {
        viewModelScope.launch {
            searchSingleImageUseCase.invoke(imageId = imageId).map { resources ->
                when (resources) {
                    is Resource.Success -> {
                        resources.data?.let { image ->
                            ImagePreviewScreenState.Success(image)
                        } ?: ImagePreviewScreenState.Error("Empty response")
                    }

                    is Resource.Error -> {
                        ImagePreviewScreenState.Error(resources.message ?: "Unknown Error")
                    }

                    is Resource.Loading -> {
                        ImagePreviewScreenState.Loading
                    }
                }
            }
        }
    }

    fun onEvent(event: ImagePreviewEvent) {
        when (event) {
            is ImagePreviewEvent.OnDownloadImage -> TODO()
            is ImagePreviewEvent.OnSaveImage -> TODO()
            is ImagePreviewEvent.OnShareImage -> TODO()
        }
    }


    // State definition
    sealed interface ImagePreviewScreenState {
        data object Loading : ImagePreviewScreenState
        data class Success(val images: ImageItem) : ImagePreviewScreenState
        data class Error(val message: String) : ImagePreviewScreenState
    }

}

