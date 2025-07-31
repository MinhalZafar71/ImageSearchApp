package com.sftech.imagesearchapp.domain.use_case

import com.sftech.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject

class IsImageFavoriteUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(imageId: String): Boolean {
        val result = imageRepository.isImageFavorite(imageId = imageId)
        return result.getOrDefault(false)

    }

}