package com.sftech.imagesearchapp.presentation.image_preview

sealed class ImagePreviewEvent {

    data class OnShareImage(val imageUri: String): ImagePreviewEvent()
    data class OnDownloadImage(val imageUri: String): ImagePreviewEvent()
    data class OnSaveImage(val imageId: Int): ImagePreviewEvent()
    data object OnBackButtonClick: ImagePreviewEvent()
}