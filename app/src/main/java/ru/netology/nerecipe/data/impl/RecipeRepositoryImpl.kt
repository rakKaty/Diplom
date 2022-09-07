package ru.netology.nerecipe.data.impl

import androidx.lifecycle.map
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nmedia.db.RecipeDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel

class RecipeRepositoryImpl(private val dao: RecipeDao) :
    RecipeRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }


    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_POST_ID) dao.insert(recipe.toEntity())
        else dao.updateContentById(recipe.id, recipe.content)
    }

    override fun like(recipeId: Long) {
        dao.likeById(recipeId)
    }

    override fun share(recipeId: Long) {
        dao.share(recipeId)
    }

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }
}