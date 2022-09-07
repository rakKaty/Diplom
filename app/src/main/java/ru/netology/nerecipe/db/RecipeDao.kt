package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC") //здесь posts - имя таблицы из PostEntity
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert
    fun insert(post: RecipeEntity)

    @Query("UPDATE recipes SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)


    @Query("""
        UPDATE recipes SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun likeById(id: Long)

    @Query("""
        UPDATE recipes SET
        shares = shares + 1
        WHERE id = :id
        """)
    fun share(id: Long)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)
}