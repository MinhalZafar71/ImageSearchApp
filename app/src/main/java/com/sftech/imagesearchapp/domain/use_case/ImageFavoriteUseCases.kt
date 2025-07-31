package com.sftech.imagesearchapp.domain.use_case

data class ImageFavoriteUseCases(
    val addImageToFavoriteUseCase: AddImageToFavoriteUseCase,
    val removeImageFromFavoriteUseCase: RemoveImageFromFavoriteUseCase,
    val isImageFavoriteUseCase: IsImageFavoriteUseCase
)