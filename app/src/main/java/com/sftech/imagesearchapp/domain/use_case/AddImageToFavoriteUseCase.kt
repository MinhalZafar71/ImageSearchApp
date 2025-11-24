package com.sftech.imagesearchapp.domain.use_case

import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject

class AddImageToFavoriteUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(imageItem: ImageItem){
        imageRepository.addImageToFavorites(imageItem = imageItem)
    }
}