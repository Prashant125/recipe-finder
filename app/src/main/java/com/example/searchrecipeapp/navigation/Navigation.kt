package com.example.searchrecipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.common.navigation.NavigationSubGraphRoutes

@Composable
fun RecipeNavigation(modifier: Modifier,navigationSubGraph: NavigationSubGraph) {

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = NavigationSubGraphRoutes.Search.route) {

        navigationSubGraph.searchFeatureApi.registerGraph(
            navigationGraphBuilder = this,
            navHostController = navHostController
        )
        navigationSubGraph.mediaPlayerApi.registerGraph(
            navigationGraphBuilder = this,
            navHostController = navHostController
        )
    }
}