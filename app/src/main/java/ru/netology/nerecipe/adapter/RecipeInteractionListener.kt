package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.Recipe

interface RecipeInteractionListener {

    fun onLikeClicked(recipe: Recipe)
    fun onShareClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onFavouriteClicked(recipe: Recipe)

    fun onCreateNewRecipeNew(
        recipeName: String,
        content: String,
        authorName: String,
        recipeCategory: String,
        photo: String?
    )

    fun onRecipeClicked(id: Long)
}