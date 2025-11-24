package com.sftech.imagesearchapp.presentation.image_preview

import com.sftech.imagesearchapp.domain.model.ImageItem

sealed class ImagePreviewEvent {
    data class OnShareImage(
        val imageItem: ImageItem,
    ) : ImagePreviewEvent()

    data class OnDownloadImage(
        val imageItem: ImageItem,
    ) : ImagePreviewEvent()

    data class OnSetWallpaper(
        val imageItem: ImageItem,
    ) : ImagePreviewEvent()

    data class OnToggleFavoriteImage(
        val imageItem: ImageItem,
    ) : ImagePreviewEvent()

    object OnBackButtonClick : ImagePreviewEvent()
}
