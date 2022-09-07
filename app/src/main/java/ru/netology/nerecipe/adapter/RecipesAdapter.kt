package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.RecipeBinding


internal class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(DiffCallback) {


    class RecipeViewHolder(
        private val binding: RecipeBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likesIcon.setOnClickListener { listener.onLikeClicked(recipe) }
            binding.shareIcon.setOnClickListener { listener.onShareClicked(recipe) }
            binding.recipePhoto.setOnClickListener { listener.onPlayVideoClicked(recipe) }
            binding.options.setOnClickListener { popupMenu.show() }
        }

        init {
            binding.recipeText.setOnClickListener { listener.onPostClicked(recipe.id) }
            binding.authorName.setOnClickListener { listener.onPostClicked(recipe.id) }
            binding.authorName.setOnClickListener { listener.onPostClicked(recipe.id) }
        }


        fun bind(recipe: Recipe) {
            this.recipe = recipe

            with(binding) {
                recipeName.text = recipe.recipeName
                recipeText.text = recipe.content
                authorName.text = recipe.authorName
                category.text = recipe.recipeCategory.toString()
                likesIcon.isChecked = recipe.likedByMe
                likesIcon.text = changeFormatOfNumberToText(getAmountOfLikes(recipe))
                shareIcon.text = changeFormatOfNumberToText(recipe.shares)
            }
        }

        private fun getAmountOfLikes(recipe: Recipe) =
            if (recipe.likedByMe) {
                recipe.likes + 1
            } else recipe.likes


        private fun changeFormatOfNumberToText(number: Int): String = when {
            number < 1000 -> "$number"
            number in 1000..9999 -> "${number / 1000}.${(number % 1000) / 100}K"
            number in 10_000..999_999 -> "${number / 1000}K"
            number in 1_000_000..9_999_999 -> "${number / 1_000_000}.${(number % 1_000_000) / 100}M"
            else -> "${number / 1_000_000}M"
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}
