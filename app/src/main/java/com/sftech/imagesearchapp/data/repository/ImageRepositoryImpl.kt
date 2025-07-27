package com.sftech.imagesearchapp.data.repository

import com.sftech.imagesearchapp.BuildConfig
import com.sftech.imagesearchapp.data.remote.OpenImageApi
import com.sftech.imagesearchapp.domain.mappers.toDomainList
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import javax.inject.Inject


class ImageRepositoryImpl @Inject constructor(
    private val api: OpenImageApi
) : ImageRepository {
    override suspend fun searchImage(query: String): Result<List<ImageItem>> {
        return try {
            val searchDto = api.searchImages(query = query, apiKey = BuildConfig.API_KEY, imageType = "all")
            Result.success(searchDto.toDomainList())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun searchSingleImage(imageId: String): Result<ImageItem> {
        return try {
            val searchDto = api.searchSingleImage(id = imageId, apiKey = BuildConfig.API_KEY, imageType = "all")
            Result.success(searchDto.toDomainList().first())
        }catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }
}