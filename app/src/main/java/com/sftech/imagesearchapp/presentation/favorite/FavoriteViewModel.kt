package com.sftech.imagesearchapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.use_case.GetFavoriteImagesUseCase
import com.sftech.imagesearchapp.presentation.image_preview.ImagePreviewEvent
import com.sftech.imagesearchapp.presentation.navigation.Screen.PreviewImage
import com.sftech.imagesearchapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteImagesUseCase: GetFavoriteImagesUseCase
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _onClickEvents = MutableSharedFlow<ImagePreviewEvent>()
    val onClickEvents: SharedFlow<ImagePreviewEvent> = _onClickEvents

    val favoriteImages: StateFlow<List<ImageItem>> = getFavoriteImagesUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _layoutType = MutableStateFlow(LayoutType.LIST)
    val layoutType: StateFlow<LayoutType> = _layoutType


    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.ChangeLayout -> {
                _layoutType.value = event.layoutType
            }

            is FavoriteEvent.DownloadImage -> {
                viewModelScope.launch {
                    _onClickEvents.emit(ImagePreviewEvent.OnDownloadImage(event.image))
                }
            }
            is FavoriteEvent.ShareImage -> {
             viewModelScope.launch {
                 _onClickEvents.emit(ImagePreviewEvent.OnShareImage(event.image))
             }
            }
        }
    }

    fun onPreviewImageClick(imageId: String) {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate(PreviewImage.createRoute(imageId)))
        }
    }


}


sealed class FavoriteEvent {
    data class ChangeLayout(val layoutType: LayoutType) : FavoriteEvent()
    data class DownloadImage(val image: ImageItem) : FavoriteEvent()
    data class ShareImage(val image: ImageItem) : FavoriteEvent()
}

enum class LayoutType(val icon: Int, val label: String) {
    LIST(R.drawable.icon_viewcomfy, "List View"), GRID(
        R.drawable.ic_grid,
        "Grid View"
    ),
    STAGGERED(R.drawable.ic_staggered, "Staggered View")
}