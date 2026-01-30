package com.sftech.imagesearchapp.di

import com.sftech.imagesearchapp.data.repository.ImageRepositoryImpl
import com.sftech.imagesearchapp.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: ImageRepositoryImpl): ImageRepository
}


