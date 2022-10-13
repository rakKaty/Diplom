package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.util.SingleLiveEvent
import ru.netology.nerecipe.db.AppDb


class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )


    val data by repository::data
    //val data get() = repository.data  - тоже самое

    val shareRecipeContent = SingleLiveEvent<Recipe>()
    val navigateToRecipeAddOrEditScreen = SingleLiveEvent<Long?>()
    val navigateToDetailedRecipeScreen = SingleLiveEvent<Long>()


    private val currentRecipe = MutableLiveData<Recipe?>(null)


    override fun onCreateNewRecipeNew(
        recipeName: String,
        content: String,
        authorName: String,
        recipeCategory: String,
        photo: String?
    ) {
        if (recipeName.isBlank() || content.isBlank()) return
        val newRecipe = currentRecipe.value?.copy(
            recipeName = recipeName,
            content = content,
            authorName = authorName,
            photo = photo

        ) ?: Recipe( // если current.value null, то мы в режиме создания поста, а не редактирования
            id = RecipeRepository.NEW_POST_ID,
            recipeName = recipeName,
            content = content,
            authorName = authorName,
            recipeCategory = recipeCategory,
            photo = photo
        )
        repository.save(newRecipe)
        currentRecipe.value = null
    }

    fun searchRecipeByName(recipeName: String) = repository.search(recipeName)

    fun searchRecipeByCategory(recipeCategory: String) = repository.search(recipeCategory)

    //region PostInteractionListener
    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id)

    override fun onFavouriteClicked(recipe: Recipe) {
        repository.favourite(recipe.id)
    }

    override fun onShareClicked(recipe: Recipe) {
        repository.share(recipe.id)
        shareRecipeContent.value = recipe
    }

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeAddOrEditScreen.value =
            recipe.id
    }

    override fun onRecipeClicked(id: Long) {
        navigateToDetailedRecipeScreen.value = id
    }

    //endregion
}