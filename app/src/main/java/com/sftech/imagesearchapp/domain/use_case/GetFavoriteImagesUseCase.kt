package com.sftech.imagesearchapp.domain.use_case

import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(): Flow<List<ImageItem>> {
        return imageRepository.getFavoriteImageList()
    }

}