package com.sftech.imagesearchapp.presentation.image_preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.use_case.ImageFavoriteUseCases
import com.sftech.imagesearchapp.domain.use_case.SearchSingleImageUseCase
import com.sftech.imagesearchapp.util.Resource
import com.sftech.imagesearchapp.util.UiEvent
import com.sftech.imagesearchapp.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePreviewViewModel
@Inject constructor(
    private val searchSingleImageUseCase: SearchSingleImageUseCase,
    private val imageFavoriteUseCases: ImageFavoriteUseCases,
) : ViewModel() {
    private val _previewScreenState =
        MutableStateFlow<ImagePreviewScreenState>(ImagePreviewScreenState.Loading)
    val imagePreviewScreenState: StateFlow<ImagePreviewScreenState> =
        _previewScreenState.asStateFlow()

    private val _onClickEvents = MutableSharedFlow<ImagePreviewEvent>()
    val onClickEvents: SharedFlow<ImagePreviewEvent> = _onClickEvents

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _isLoadingFavorite = MutableStateFlow(false)
    val isLoadingFavorite: StateFlow<Boolean> = _isLoadingFavorite.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadImageDetails(imageId: String) {
        viewModelScope.launch {
            launch { loadFavoriteStatus(imageId) }

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
                }.collect { screenState ->
                    _previewScreenState.value = screenState
                }
        }
    }

    suspend fun loadFavoriteStatus(imageId: String) {
        val isFavorite = imageFavoriteUseCases.isImageFavoriteUseCase.invoke(imageId)
        _isFavorite.value = isFavorite
    }

    fun onEvent(event: ImagePreviewEvent) {
        when (event) {
            is ImagePreviewEvent.OnDownloadImage -> {
                viewModelScope.launch {
                    _onClickEvents.emit(ImagePreviewEvent.OnDownloadImage(event.imageItem))
                }
            }

            is ImagePreviewEvent.OnToggleFavoriteImage -> {
                onToggleFavorite(event.imageItem)
            }

            is ImagePreviewEvent.OnShareImage -> {
                viewModelScope.launch {
                    _onClickEvents.emit(ImagePreviewEvent.OnShareImage(event.imageItem))
                }
            }

            ImagePreviewEvent.OnBackButtonClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateUp)
                }
            }

            is ImagePreviewEvent.OnSetWallpaper -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowSnackBar(UiText.DynamicString("On Wallpaper Clicked")))
                }
            }
        }
    }

    private fun onToggleFavorite(imageItem: ImageItem) {
        viewModelScope.launch {
            try {
                _isLoadingFavorite.value = true

                val currentState = _isFavorite.value

                if (currentState) {
                    imageFavoriteUseCases.removeImageFromFavoriteUseCase.invoke(imageItem)
                    _isFavorite.value = false
                } else {
                    imageFavoriteUseCases.addImageToFavoriteUseCase.invoke(imageItem)
                    _isFavorite.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoadingFavorite.value = false
            }
        }
    }

    // State definition
    sealed interface ImagePreviewScreenState {
        data object Loading : ImagePreviewScreenState

        data class Success(
            val images: ImageItem,
        ) : ImagePreviewScreenState

        data class Error(
            val message: String,
        ) : ImagePreviewScreenState
    }
}
