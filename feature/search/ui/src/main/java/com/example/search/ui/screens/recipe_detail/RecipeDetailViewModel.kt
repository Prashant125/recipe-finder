package com.example.search.ui.screens.recipe_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.search.domain.model.Recipe
import com.example.search.domain.model.RecipeDetails
import com.example.search.domain.use_cases.DeleteRecipeUseCase
import com.example.search.domain.use_cases.GetRecipeDetailsUseCase
import com.example.search.domain.use_cases.InsertRecipeUseCase
import com.example.utils.NetworkResult
import com.example.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase

): ViewModel() {

    private val _uiState  = MutableStateFlow(RecipeDetail.UiState())
    val uiState: StateFlow<RecipeDetail.UiState> get() = _uiState.asStateFlow()

    private val _navigation = Channel<RecipeDetail.Navigation>()
    val navigation: Flow<RecipeDetail.Navigation> get() = _navigation.receiveAsFlow()

    private fun getRecipeDetail(id: String) = getRecipeDetailsUseCase.invoke(id)
        .onEach { result ->
            when(result) {
                is NetworkResult.Success -> _uiState.update { RecipeDetail.UiState(data = result.data)}
                is NetworkResult.Error -> _uiState.update { RecipeDetail.UiState(error = UiText.RemoteString(result.message.toString()))}
                is NetworkResult.Loading -> _uiState.update{ RecipeDetail.UiState(isLoading = true)}
            }
        }.launchIn(viewModelScope)

    fun onEvent(event: RecipeDetail.Event) {
        when(event) {
            is RecipeDetail.Event.FetchRecipeDetail -> getRecipeDetail(event.id)
            RecipeDetail.Event.GoToRecipeListScreen -> {
                viewModelScope.launch {
                    _navigation.send(RecipeDetail.Navigation.GoToRecipeDetailScreen)
                }
            }

            is RecipeDetail.Event.DeleteRecipeDetail -> {
                deleteRecipeUseCase.invoke(event.recipeDetails.toRecipe()).launchIn(viewModelScope)
            }
            is RecipeDetail.Event.InsertRecipeDetail -> {
                insertRecipeUseCase.invoke(event.recipeDetails.toRecipe()).launchIn(viewModelScope )
            }

            is RecipeDetail.Event.GoToMediaPlayer -> viewModelScope.launch {
                _navigation.send(RecipeDetail.Navigation.GoTOMediaPlayer(event.youtubeUrl))
            }
        }
    }

    fun RecipeDetails.toRecipe(): Recipe {
        return Recipe(
            idMeal,
            strArea,
            strMeal,
            strMealThumb,
            strCategory,
            strTags,
            strYoutube,
            strInstructions,
        )

    }

        object RecipeDetail {
            data class UiState(
                val isLoading: Boolean = false,
                val error: UiText = UiText.Idle,
                val data: RecipeDetails? = null
            )


            sealed interface Navigation {

                data object GoToRecipeDetailScreen : Navigation
                data class GoTOMediaPlayer(val youtubeUrl: String): Navigation
            }


            sealed interface Event {
                data class FetchRecipeDetail(val id: String) : Event
                data object GoToRecipeListScreen: Event
                data class InsertRecipeDetail(val recipeDetails: RecipeDetails): Event
                data class DeleteRecipeDetail(val recipeDetails: RecipeDetails): Event
                data class GoToMediaPlayer(val youtubeUrl: String): Event
            }

        }
}