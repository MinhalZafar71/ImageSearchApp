package com.sftech.imagesearchapp.di

import com.sftech.imagesearchapp.data.remote.OpenImageApi
import com.sftech.imagesearchapp.data.remote.OpenImageApi.Companion.BASE_URL
import com.sftech.imagesearchapp.data.repository.ImageRepositoryImpl
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import com.sftech.imagesearchapp.domain.use_case.GetImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Singleton
    @Provides
    fun provideOpenImageApi(): OpenImageApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(OpenImageApi::class.java)
    }

    @Singleton
    @Provides
    fun provideImageRepository(api: OpenImageApi): ImageRepository {
        return ImageRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideGetImageUseCase(imageRepository: ImageRepository): GetImageUseCase{
        return GetImageUseCase(imageRepository)
    }
}