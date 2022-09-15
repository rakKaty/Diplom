package ru.netology.nerecipe.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.RecipeEntity
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel

class RecipeRepositoryImpl(private val dao: RecipeDao) :
    RecipeRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }


    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_POST_ID) dao.insert(recipe.toEntity())
        else dao.updateContentById(recipe.id, recipe.content, recipe.recipeName, recipe.recipeCategory, recipe.photo)
    }


    override fun like(recipeId: Long) {
        dao.likeById(recipeId)
    }

    override fun favourite(recipeId: Long) {
        dao.favouriteById(recipeId)
    }

    override fun share(recipeId: Long) {
        dao.share(recipeId)
    }

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }
}