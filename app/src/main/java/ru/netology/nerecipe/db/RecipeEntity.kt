package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nerecipe.CategoryType


@Entity(tableName = "recipes")
class RecipeEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id")
    val id: Long,

    @ColumnInfo(name= "recipeName")
    val recipeName: String,

    @ColumnInfo(name= "content")
    val content: String,

    @ColumnInfo(name= "authorName")
    val authorName: String,

    @ColumnInfo(name= "CategoryType")
    val CategoryType: CategoryType,

    @ColumnInfo(name= "likes")
    var likes: Int = 0,

    @ColumnInfo(name= "likedByMe")
    val likedByMe: Boolean = false,

    @ColumnInfo(name= "shares")
    var shares: Int = 999,

    @ColumnInfo(name= "photo")
    val photo: String?
)