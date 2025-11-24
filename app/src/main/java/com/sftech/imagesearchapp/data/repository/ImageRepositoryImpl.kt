package com.sftech.imagesearchapp.data.repository

import com.sftech.imagesearchapp.BuildConfig
import com.sftech.imagesearchapp.data.local.FavoriteDao
import com.sftech.imagesearchapp.data.remote.OpenImageApi
import com.sftech.imagesearchapp.domain.mappers.toDomain
import com.sftech.imagesearchapp.domain.mappers.toDomainList
import com.sftech.imagesearchapp.domain.mappers.toFavoriteEntity
import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageRepositoryImpl
@Inject constructor(
    private val api: OpenImageApi,
    private val dao: FavoriteDao,
) : ImageRepository {
    override suspend fun searchImage(query: String): Result<List<ImageItem>> = try {
        val searchDto =
            api.searchImages(query = query, apiKey = BuildConfig.API_KEY, imageType = "all")
        Result.success(searchDto.toDomainList())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun searchSingleImage(imageId: String): Result<ImageItem> = try {
        val searchDto =
            api.searchSingleImage(id = imageId, apiKey = BuildConfig.API_KEY, imageType = "all")
        Result.success(searchDto.toDomainList().first())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override suspend fun addImageToFavorites(imageItem: ImageItem) {
        try {
            dao.addToFavorite(imageItem.toFavoriteEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeImageFromFavorites(imageId: String) {
        try {
            val id = Integer.parseInt(imageId)
            dao.deleteFromFavorite(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun isImageFavorite(imageId: String): Result<Boolean> = try {
        val id = Integer.parseInt(imageId)
        val result = dao.isFavorite(id)
        Result.success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override fun getFavoriteImageList(): Flow<List<ImageItem>> =
        dao.getAllFavoritesFlow().map { favorites ->
            favorites.map { it.toDomain() }
        }.catch { e ->
            e.printStackTrace()
            emit(emptyList())
        }
}
