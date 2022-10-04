package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.RecipeFragment.Companion.idArg
import ru.netology.nerecipe.adapter.PreviewRecipesAdapter
import ru.netology.nerecipe.databinding.FragmentFeedBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class FeedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)


        val adapter = PreviewRecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { previewRecipes ->
            adapter.submitList(previewRecipes)
        }


        viewModel.shareRecipeContent.observe(viewLifecycleOwner) { recipe ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, recipe.content)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }


        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newRecipeFragment)
        }


        viewModel.navigateToRecipeAddOrEditScreen.observe(viewLifecycleOwner) { recipeId ->
            findNavController().navigate(
                R.id.action_feedFragment_to_newRecipeFragment,
                Bundle().apply { idArg = recipeId }
            )
        }


        viewModel.navigateToDetailedRecipeScreen.observe(viewLifecycleOwner) { recipeId ->
            findNavController().navigate(R.id.action_feedFragment_to_recipeFragment,
                Bundle().apply { idArg = recipeId })
        }

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.recipesRecyclerView.layoutManager = manager




        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.favourite -> {

                    binding.fab.visibility = View.GONE
                    binding.chipGroupCategoryFilters.visibility = View.GONE
                    binding.chipGroupCategoryFilters.clearCheck()

                    viewModel.data.observe(viewLifecycleOwner) { recipes ->
                        val favouriteRecipes = recipes.filter { it.favouriteByMe }
                        adapter.submitList(favouriteRecipes)

                        val emptyList = recipes.none { it.favouriteByMe }
                        binding.textBackground.visibility =
                            if (emptyList) View.VISIBLE else View.GONE
                    }
                    true
                }

                R.id.home -> {

                    binding.chipGroupCategoryFilters.visibility = View.GONE
                    binding.chipGroupCategoryFilters.clearCheck()

                    viewModel.data.observe(viewLifecycleOwner) { recipes ->
                        binding.fab.visibility = View.VISIBLE
                        binding.textBackground.visibility = View.GONE
                        adapter.submitList(recipes)
                    }
                    true
                }

                R.id.filters -> {

                    binding.chipGroupCategoryFilters.visibility = View.VISIBLE
                    viewModel.data.observe(viewLifecycleOwner) { previewRecipes ->
                        adapter.submitList(previewRecipes)
                    }

                    binding.chipGroupCategoryFilters.setOnCheckedStateChangeListener { _, _ ->

                        fun chipIdToText(chipId: Int): String {
                            return when (chipId) {
                                binding.categoryStrokeEuropean.id -> "European"
                                binding.categoryStrokeAsian.id -> "Asian"
                                binding.categoryStrokeEastern.id -> "Eastern"
                                binding.categoryStrokeAmerican.id -> "American"
                                binding.categoryStrokeRussian.id -> "Russian"
                                binding.categoryStrokeMediterranean.id -> "Mediterranean"
                                binding.categoryStrokePanasian.id -> "Pan-Asian"
                                else -> "Без категории"
                            }
                        }

                        val chosenRecipeCategory =
                            chipIdToText(binding.chipGroupCategoryFilters.checkedChipId)

                        viewModel.data.observe(viewLifecycleOwner) { recipes ->
                            val recipesByCategory = recipes.filter {
                                it.recipeCategory == chosenRecipeCategory
                            }
                            adapter.submitList(recipesByCategory)

                            if (recipesByCategory.isEmpty()) {
                                Toast.makeText(context, "Результатов нет", Toast.LENGTH_SHORT)
                                    .show()
                                adapter.submitList(recipesByCategory)
                            } else {
                                adapter.submitList(recipesByCategory)
                            }

                        }

                    }

                    true
                }
                else -> false
            }
        }



    /* Не реботает
        binding.searchView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.chipGroupCategoryFilters.visibility = View.GONE
            viewModel.data.observe(viewLifecycleOwner) { previewRecipes ->
                adapter.submitList(previewRecipes)
            }
        } */

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    adapter.submitList(viewModel.data.value)
                    return true
                }

                binding.chipGroupCategoryFilters.visibility = View.GONE
                binding.chipGroupCategoryFilters.clearCheck()

                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    val recipesSearchByName = recipes.filter { recipe ->
                        recipe.recipeName.lowercase().contains(newText.lowercase())
                    }
                    viewModel.searchRecipeByName(newText)


                    if (recipesSearchByName.isEmpty()) {
                        Toast.makeText(context, "Результатов нет", Toast.LENGTH_SHORT).show()
                        adapter.submitList(recipesSearchByName)
                    } else {
                        adapter.submitList(recipesSearchByName)
                    }
                }
                return true
            }
        }
        )


        var touchCallBack: ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeFlag(
                    ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP
                            or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (viewHolder.itemViewType != target.itemViewType)
                    return false

                val from = viewHolder.bindingAdapterPosition
                val to = target.bindingAdapterPosition


                val list = adapter.currentList.toMutableList()

                val item = list.removeAt(from)
                list.add(to, item)

                adapter.submitList(list)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        val touchHelper = ItemTouchHelper(touchCallBack)
        touchHelper.attachToRecyclerView(binding.recipesRecyclerView)


        return binding.root
    }


}