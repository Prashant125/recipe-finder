package com.example.media_player.di

import com.example.media_player.navigation.MediaPlayerFeatureApi
import com.example.media_player.navigation.MediaPlayerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MediaPlayerModule {

    @Provides
    fun provideMediaPlayerFeatureApi(): MediaPlayerFeatureApi {
        return MediaPlayerImpl()
    }
}