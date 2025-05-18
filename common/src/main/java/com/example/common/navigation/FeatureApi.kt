package com.example.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureApi {

    // will register sub graphs using this function
    fun registerGraph(
        navigationGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    )
}