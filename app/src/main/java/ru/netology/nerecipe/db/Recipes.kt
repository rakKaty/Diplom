package ru.netology.nmedia.db

import ru.netology.nerecipe.Recipe


internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    recipeName = recipeName,
    content = content,
    authorName = authorName,
    recipeCategory = CategoryType,
    likes = likes,
    likedByMe =  likedByMe,
    shares = shares,
    photo = photo
)


internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    recipeName = recipeName,
    content = content,
    authorName = authorName,
    CategoryType = recipeCategory,
    likes = likes,
    likedByMe =  likedByMe,
    shares = shares,
    photo = photo
)