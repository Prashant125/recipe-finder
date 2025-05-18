package com.example.searchrecipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.searchrecipeapp.navigation.NavigationSubGraph
import com.example.searchrecipeapp.navigation.RecipeNavigation
import com.example.searchrecipeapp.ui.theme.SearchRecipeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationSubGraph: NavigationSubGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchRecipeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.safeContentPadding()) {
                    RecipeNavigation(modifier = Modifier,navigationSubGraph = navigationSubGraph)
                }
            }
        }
    }
}