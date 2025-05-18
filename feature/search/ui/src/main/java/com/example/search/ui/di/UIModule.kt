package com.example.search.ui.di

import com.example.search.ui.navigation.SearchFeatureApi
import com.example.search.ui.navigation.SearchFeatureApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object UIModule {

    @Provides
    fun provideSearchFeatureApiImpl(): SearchFeatureApi {
        return SearchFeatureApiImpl()
    }
}