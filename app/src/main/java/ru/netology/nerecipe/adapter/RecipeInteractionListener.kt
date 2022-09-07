package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.Recipe

interface RecipeInteractionListener {

    fun onLikeClicked(recipe: Recipe)
    fun onShareClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onCreateNewPost(newPostContent : String)
    fun onPlayVideoClicked(recipe: Recipe)
    fun onPostClicked(id: Long)
}