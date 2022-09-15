package ru.netology.nerecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


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

    @ColumnInfo(name= "categoryType")
    val categoryType: String,

    @ColumnInfo(name= "likes")
    var likes: Int = 0,

    @ColumnInfo(name= "likedByMe")
    val likedByMe: Boolean = false,

    @ColumnInfo(name= "favouriteByMe")
    val favouriteByMe: Boolean = false,

    @ColumnInfo(name= "shares")
    var shares: Int = 0,

    @ColumnInfo(name= "photo")
    val photo: String?
)