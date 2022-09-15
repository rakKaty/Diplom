package ru.netology.nerecipe

import android.os.Parcelable
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize

data class Recipe(
    val id: Long,
    val recipeName: String,
    val content: String,
    val authorName: String,
    val recipeCategory: String,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    val favouriteByMe: Boolean = false,
    var shares: Int = 0,
    val photo: String?
) : Parcelable



