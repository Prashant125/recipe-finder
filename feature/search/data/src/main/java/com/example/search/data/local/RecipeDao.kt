package com.example.search.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.search.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("Select * from Recipe")
    fun getAllRecipe():Flow<List<Recipe>>

    @Update
    suspend fun updateRecipe(recipe: Recipe)

}