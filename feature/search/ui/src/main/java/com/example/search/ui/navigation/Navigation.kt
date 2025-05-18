package com.example.search.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.common.navigation.FeatureApi
import com.example.common.navigation.NavigationRoutes
import com.example.common.navigation.NavigationSubGraphRoutes
import com.example.search.ui.screens.favourite.FavouriteScreen
import com.example.search.ui.screens.favourite.FavouriteViewModel
import com.example.search.ui.screens.recipe_detail.RecipeDetailScreen
import com.example.search.ui.screens.recipe_detail.RecipeDetailViewModel
import com.example.search.ui.screens.recipe_list.RecipeListScreen
import com.example.search.ui.screens.recipe_list.RecipeListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

interface SearchFeatureApi : FeatureApi

class SearchFeatureApiImpl() : SearchFeatureApi {
    override fun registerGraph(
        navigationGraphBuilder: androidx.navigation.NavGraphBuilder,
        navHostController: androidx.navigation.NavHostController
    ) {
        navigationGraphBuilder.navigation(startDestination = NavigationRoutes.RecipeList.route, route = NavigationSubGraphRoutes.Search.route) {

            composable(route = NavigationRoutes.RecipeList.route) {
                val viewModel = hiltViewModel<RecipeListViewModel>()
                RecipeListScreen(modifier = Modifier,viewModel =viewModel,navHostController = navHostController ) { mealId ->
                    viewModel.onEvent(RecipeListViewModel.RecipeList.Event.GoTORecipeDetailScreen(mealId))
                }
            }

            composable(route = NavigationRoutes.RecipeDetails.route) {
                val viewModel = hiltViewModel<RecipeDetailViewModel>()
                val mealId = it.arguments?.getString("id")
                LaunchedEffect(key1 = mealId) {
                    mealId?.let { meal ->
                        viewModel.onEvent(RecipeDetailViewModel.RecipeDetail.Event.FetchRecipeDetail(meal))
                    }
                }
                RecipeDetailScreen(viewModel = viewModel, navHostController = navHostController,
                    onNavigationClick = {
                        viewModel.onEvent(RecipeDetailViewModel.RecipeDetail.Event.GoToRecipeListScreen)
                    }, onDelete = {
                        viewModel.onEvent(RecipeDetailViewModel.RecipeDetail.Event.DeleteRecipeDetail(it))
                    }, onFavourite = {
                        viewModel.onEvent(RecipeDetailViewModel.RecipeDetail.Event.InsertRecipeDetail(it))
                    })
            }
            composable(NavigationRoutes.FavouriteScreen.route) {
                val viewModel = hiltViewModel<FavouriteViewModel>()
                FavouriteScreen(
                    navHostController = navHostController,
                    viewModel = viewModel,
                    onClick = { mealId ->
                        viewModel.onEvent(FavouriteViewModel.FavouriteScreen.Event.GoToDetails(mealId))
                    }
                )
            }
        }
    }
}