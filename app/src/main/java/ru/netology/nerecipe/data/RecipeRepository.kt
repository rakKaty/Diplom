package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.Recipe

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    fun like(recipeId: Long)
    fun favourite(recipeId: Long)
    fun share(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun search(recipeName: String)


    companion object {
        const val NEW_POST_ID = 0L
    }
}