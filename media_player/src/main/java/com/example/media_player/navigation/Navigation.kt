package com.example.media_player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.common.navigation.FeatureApi
import com.example.common.navigation.NavigationRoutes
import com.example.common.navigation.NavigationSubGraphRoutes
import com.example.media_player.screens.MediaPlayerScreen

interface MediaPlayerFeatureApi : FeatureApi

class MediaPlayerImpl: MediaPlayerFeatureApi {
    override fun registerGraph(
        navigationGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navigationGraphBuilder.navigation(route = NavigationSubGraphRoutes.MediaPlayer.route, startDestination = NavigationRoutes.MediaPlayer.route) {

            composable(route = NavigationRoutes.MediaPlayer.route) {
                val mediaPlayerVideoId = it.arguments?.getString("video_id")
                mediaPlayerVideoId?.let {
                    MediaPlayerScreen(videoId = it)
                }
            }
        }
    }

}