package com.sftech.imagesearchapp.domain.repository

import com.sftech.imagesearchapp.domain.model.ImageItem
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun searchImage(query: String): Result<List<ImageItem>>

    suspend fun searchSingleImage(imageId: String): Result<ImageItem>

    suspend fun addImageToFavorites(imageItem: ImageItem)

    suspend fun removeImageFromFavorites(imageId: String)

    suspend fun isImageFavorite(imageId: String): Result<Boolean>

    fun getFavoriteImageList(): Flow<List<ImageItem>>

}