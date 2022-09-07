package ru.netology.nerecipe

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long,
    val recipeName: String,
    val content: String,
    val authorName: String,
    val recipeCategory: CategoryType,
    var likes: Int = 0,
    val likedByMe: Boolean = false,
    var shares: Int = 999,
    val photo: String? = "https://www.youtube.com/watch?v=boO-OdBnTic"
)

enum class CategoryType {
    European, Asian, Eastern, American, Russian, Mediterranean, Panasian
}


