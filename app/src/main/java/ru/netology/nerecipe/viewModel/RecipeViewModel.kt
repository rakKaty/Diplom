package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nerecipe.CategoryType
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.util.SingleLiveEvent
import ru.netology.nmedia.db.AppDb


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
    val navigateToPostContentScreen = SingleLiveEvent<String?>()
    val navigateToPostScreen = SingleLiveEvent<Long>()

    /**
     * Значение события содержит url видео для воспроизведения
     */
    val playVideo = SingleLiveEvent<String>()

    private val currentRecipe = MutableLiveData<Recipe?>(null)

    override fun onCreateNewPost(newPostContent: String) {
        if (newPostContent.isBlank()) return
        val recipe = currentRecipe.value?.copy(
            content = newPostContent
        ) ?: Recipe( // если current.value null, то мы в режиме создания поста, а не редактирования
            id = RecipeRepository.NEW_POST_ID,
            recipeName = "Recipe name",
            content = newPostContent,
            authorName = "Author name",
            recipeCategory = CategoryType.Asian
        )
        repository.save(recipe)
        currentRecipe.value = null
    }


    //region PostInteractionListener
    override fun onLikeClicked(recipe: Recipe) = repository.like(recipe.id)

    override fun onShareClicked(recipe: Recipe) {
        repository.share(recipe.id)
        shareRecipeContent.value = recipe
    }

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe // закидываем пост в поток
        navigateToPostContentScreen.value =
            recipe.content // отобразится контент текущего поста на экране
    }

    override fun onPlayVideoClicked(recipe: Recipe) {
        val url = requireNotNull(recipe.photo) {
            "Url is null"
        }
        playVideo.value = url
    }

    override fun onPostClicked(id: Long) {
        navigateToPostScreen.value = id
    }

    //endregion
}