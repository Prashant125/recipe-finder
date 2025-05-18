package com.example.searchrecipeapp.di

import android.content.Context
import com.example.media_player.navigation.MediaPlayerFeatureApi
import com.example.search.data.local.RecipeDao
import com.example.search.ui.navigation.SearchFeatureApi
import com.example.searchrecipeapp.local.AppDatabase
import com.example.searchrecipeapp.navigation.NavigationSubGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNavigationSubgraph(searchFeatureApi: SearchFeatureApi,mediaPlayerFeatureApi: MediaPlayerFeatureApi): NavigationSubGraph {
        return NavigationSubGraph(searchFeatureApi,mediaPlayerFeatureApi)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase) : RecipeDao {
        return appDatabase.getRecipeDao()
    }
}