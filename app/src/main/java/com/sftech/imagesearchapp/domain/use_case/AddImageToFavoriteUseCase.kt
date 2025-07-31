package com.sftech.imagesearchapp.domain.use_case

import com.sftech.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject

class AddImageToFavoriteUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(imageId: String){
        imageRepository.addImageToFavorites(imageId = imageId)
    }
}