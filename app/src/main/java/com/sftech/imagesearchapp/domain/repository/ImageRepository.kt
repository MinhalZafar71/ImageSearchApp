package com.sftech.imagesearchapp.domain.repository

import com.sftech.imagesearchapp.domain.model.ImageItem

interface ImageRepository {

    suspend fun searchImage(query: String): Result<List<ImageItem>>

}