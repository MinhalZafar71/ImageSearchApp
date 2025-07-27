package com.sftech.imagesearchapp.data.remote

import com.sftech.imagesearchapp.data.remote.dto.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenImageApi {

    @GET("api/")
    suspend fun searchImages(
        @Query(value = "key") apiKey: String,
        @Query(value = "q") query: String,
        @Query(value = "image_type") imageType: String
    ): PixabayResponse


    @GET("api/")
    suspend fun searchSingleImage(
        @Query(value = "key") apiKey: String,
        @Query(value = "id") id: String,
        @Query(value = "image_type") imageType: String
    ): PixabayResponse

    companion object{
        const val BASE_URL = "https://pixabay.com/"
        const val API_KEY = "51315330-46a324aaa79419ac31852af8f"
    }
}