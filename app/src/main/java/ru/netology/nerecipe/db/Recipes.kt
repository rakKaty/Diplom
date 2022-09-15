package ru.netology.nerecipe.db

import ru.netology.nerecipe.Recipe


internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    recipeName = recipeName,
    content = content,
    authorName = authorName,
    recipeCategory = categoryType,
    likes = likes,
    likedByMe = likedByMe,
    shares = shares,
    photo = photo,
    favouriteByMe = favouriteByMe
)


internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    recipeName = recipeName,
    content = content,
    authorName = authorName,
    categoryType = recipeCategory,
    likes = likes,
    likedByMe = likedByMe,
    shares = shares,
    photo = photo,
    favouriteByMe = favouriteByMe
)