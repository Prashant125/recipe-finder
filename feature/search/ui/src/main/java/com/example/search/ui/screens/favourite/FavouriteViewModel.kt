package com.example.search.ui.screens.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.domain.model.Recipe
import com.example.search.domain.use_cases.DeleteRecipeUseCase
import com.example.search.domain.use_cases.GetAllRecipeFromLocalDbUseCase
import com.example.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel@Inject constructor(
    private val getAllRecipeFromLocalDbUseCase: GetAllRecipeFromLocalDbUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
): ViewModel() {

    private var originalList = mutableListOf<Recipe>()
    private val _uiState = MutableStateFlow(FavouriteScreen.UiState())
    val uiState: StateFlow<FavouriteScreen.UiState> get() = _uiState.asStateFlow()

    private fun deleteRecipe(recipe:Recipe) = deleteRecipeUseCase.invoke(recipe).launchIn(viewModelScope)

    private fun getRecipeList() = viewModelScope.launch {
        getAllRecipeFromLocalDbUseCase.invoke().collectLatest { list ->
            _uiState.update { FavouriteScreen.UiState(data = list) }
            originalList = list.toMutableList()
        }
    }

    private val _navigation = Channel<FavouriteScreen.Navigation>()
    val navigation : Flow<FavouriteScreen.Navigation> = _navigation.receiveAsFlow()

    init {
        getRecipeList()
    }

    fun onEvent(event: FavouriteScreen.Event) {
        when(event) {
            FavouriteScreen.Event.AlphabeticalSort -> alphabeticalSort()
            FavouriteScreen.Event.LessIngredientsSort -> lessIngredientSort()
            FavouriteScreen.Event.ResetSort -> resetSort()
            is FavouriteScreen.Event.ShowDetail -> {
                viewModelScope.launch {
                    _navigation.send(FavouriteScreen.Navigation.GoToRecipeDetailScreen(id = event.id))
                }
            }

            is FavouriteScreen.Event.DeleteRecipe -> {
                deleteRecipe(event.recipe)
            }

            is FavouriteScreen.Event.GoToDetails -> viewModelScope.launch{
                _navigation.send(FavouriteScreen.Navigation.GoToRecipeDetailScreen(event.id))
            }
        }
    }


    object FavouriteScreen {
        data class UiState(
            val isLoading: Boolean = false,
            var error: UiText = UiText.Idle,
            val data: List<Recipe>?= null
        )

        sealed interface Event{
            data object AlphabeticalSort: Event
            data object LessIngredientsSort: Event
            data object ResetSort: Event
            data class ShowDetail(val id: String): Event
            data class DeleteRecipe(val recipe: Recipe):Event
            data class GoToDetails(val id: String): Event

        }

        sealed interface Navigation {
            data class GoToRecipeDetailScreen(val id : String): Navigation
        }

    }

    fun alphabeticalSort() = _uiState.update { FavouriteScreen.UiState(data = originalList.sortedBy { it.strMeal }) }

    fun lessIngredientSort() = _uiState.update { FavouriteScreen.UiState(data = originalList.sortedBy { it.strInstructions.length }) }

    fun resetSort() = _uiState.update {FavouriteScreen.UiState(data =  originalList)}

}