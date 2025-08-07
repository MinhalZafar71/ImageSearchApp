package com.sftech.imagesearchapp.domain.use_case

import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import com.sftech.imagesearchapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchSingleImageUseCase
    @Inject
    constructor(
        val imageRepository: ImageRepository,
    ) {
        operator fun invoke(imageId: String): Flow<Resource<ImageItem>> =
            flow {
                emit(Resource.Loading(""))
                try {
                    val result = imageRepository.searchSingleImage(imageId = imageId)
                    result
                        .onSuccess { imageObject ->
                            emit(Resource.Success(imageObject))
                        }.onFailure { exception ->
                            emit(
                                Resource.Error(
                                    exception.message ?: "Unknown Error",
                                ),
                            )
                        }
                } catch (e: Exception) {
                    emit(Resource.Error(e.message))
                }
            }
    }
