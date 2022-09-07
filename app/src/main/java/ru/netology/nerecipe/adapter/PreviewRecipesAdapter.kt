package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.PreviewRecipeBinding


internal class PreviewRecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, PreviewRecipesAdapter.PreviewRecipeViewHolder>(DiffCallback) {


    class PreviewRecipeViewHolder(
        private val binding: PreviewRecipeBinding,
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
            binding.recipePhoto.setOnClickListener { listener.onPostClicked(recipe.id) }
            binding.recipeName.setOnClickListener { listener.onPostClicked(recipe.id) }
            binding.category.setOnClickListener { listener.onPostClicked(recipe.id) }
            binding.authorName.setOnClickListener { listener.onPostClicked(recipe.id) }
        }


        fun bind(recipe: Recipe) {
            this.recipe = recipe

            with(binding) {
                recipeName.text = recipe.recipeName
                authorName.text = recipe.authorName
                category.text = recipe.recipeCategory.toString()
                likesIcon.isChecked = recipe.likedByMe
                likesIcon.text = changeFormatOfNumberToText(recipe.likes)
                shareIcon.text = changeFormatOfNumberToText(recipe.shares)
            }
        }

        private fun changeFormatOfNumberToText(number: Int): String = when {
            number < 1000 -> "$number"
            number in 1000..9999 -> "${number / 1000}.${(number % 1000) / 100}K"
            number in 10_000..999_999 -> "${number / 1000}K"
            number in 1_000_000..9_999_999 -> "${number / 1_000_000}.${(number % 1_000_000) / 100}M"
            else -> "${number / 1_000_000}M"
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewRecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PreviewRecipeBinding.inflate(inflater, parent, false)
        return PreviewRecipeViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: PreviewRecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}
