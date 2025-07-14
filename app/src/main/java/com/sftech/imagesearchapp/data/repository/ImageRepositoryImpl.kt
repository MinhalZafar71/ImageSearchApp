package com.sftech.imagesearchapp.data.repository

import com.sftech.imagesearchapp.data.remote.OpenImageApi
import com.sftech.imagesearchapp.data.remote.OpenImageApi.Companion.API_KEY
import com.sftech.imagesearchapp.domain.mappers.toDomainList
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject


class ImageRepositoryImpl @Inject constructor(
    private val api: OpenImageApi
) : ImageRepository {
    override suspend fun searchImage(query: String): Result<List<ImageItem>> {
        return try {
            val searchDto = api.getQueryImage(query = query, apiKey = API_KEY, imageType = "photo")
            Result.success(searchDto.toDomainList())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}