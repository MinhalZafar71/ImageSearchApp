package com.sftech.imagesearchapp.domain.use_case

import com.sftech.imagesearchapp.domain.model.ImageItem
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import com.sftech.imagesearchapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class SearchImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    operator fun invoke(query: String): Flow<Resource<List<ImageItem>>> = flow {
        emit(Resource.Loading(""))
        try {
            val encodeQuery =  encodeQuery(query)
            val result = imageRepository.searchImage(encodeQuery)
            result
                .onSuccess { images ->
                    emit(Resource.Success(images))
                }
                .onFailure { exception ->
                    emit(Resource.Error(exception.message ?: "Unknown error"))
                }
        }catch (e: Exception){
            emit(Resource.Error(e.message))
        }
    }


    private fun encodeQuery(query: String): String {
        return URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
    }
}

