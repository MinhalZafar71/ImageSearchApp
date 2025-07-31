package com.sftech.imagesearchapp.domain.repository

import com.sftech.imagesearchapp.domain.model.ImageItem

interface ImageRepository {

    suspend fun searchImage(query: String): Result<List<ImageItem>>

    suspend fun searchSingleImage(imageId: String): Result<ImageItem>

    suspend fun addImageToFavorites(imageId: String)

    suspend fun removeImageFromFavorites(imageId: String)

    suspend fun isImageFavorite(imageId: String): Result<Boolean>

}