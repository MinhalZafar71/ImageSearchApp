package com.sftech.imagesearchapp.di

import android.app.Application
import androidx.room.Room
import com.sftech.imagesearchapp.data.local.FavoriteDao
import com.sftech.imagesearchapp.data.local.FavoriteDatabase
import com.sftech.imagesearchapp.data.remote.OpenImageApi
import com.sftech.imagesearchapp.data.remote.OpenImageApi.Companion.BASE_URL
import com.sftech.imagesearchapp.data.repository.ImageRepositoryImpl
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import com.sftech.imagesearchapp.domain.use_case.AddImageToFavoriteUseCase
import com.sftech.imagesearchapp.domain.use_case.ImageFavoriteUseCases
import com.sftech.imagesearchapp.domain.use_case.IsImageFavoriteUseCase
import com.sftech.imagesearchapp.domain.use_case.RemoveImageFromFavoriteUseCase
import com.sftech.imagesearchapp.domain.use_case.SearchImagesUseCase
import com.sftech.imagesearchapp.domain.use_case.SearchSingleImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideFavoriteDatabase(app: Application): FavoriteDatabase {
        return Room.databaseBuilder(
            app,
            FavoriteDatabase::class.java,
            "favorite_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: FavoriteDatabase): FavoriteDao {
        return database.dao
    }


    @Singleton
    @Provides
    fun provideImageRepository(api: OpenImageApi, dao: FavoriteDao): ImageRepository {
        return ImageRepositoryImpl(api, dao)
    }

    @Singleton
    @Provides
    fun provideGetImageUseCase(imageRepository: ImageRepository): SearchImagesUseCase {
        return SearchImagesUseCase(imageRepository)
    }

    @Provides
    @Singleton
    fun provideSearchSingleImageUseCase(imageRepository: ImageRepository): SearchSingleImageUseCase {
        return SearchSingleImageUseCase(imageRepository)
    }

    @Provides
    @Singleton
    fun provideImageFavoriteUseCases(imageRepository: ImageRepository): ImageFavoriteUseCases {
        return ImageFavoriteUseCases(
            addImageToFavoriteUseCase = AddImageToFavoriteUseCase(imageRepository),
            removeImageFromFavoriteUseCase = RemoveImageFromFavoriteUseCase(imageRepository),
            isImageFavoriteUseCase = IsImageFavoriteUseCase(imageRepository)
        )
    }


}