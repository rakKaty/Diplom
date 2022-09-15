package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.RecipeFragment.Companion.idArg
import ru.netology.nerecipe.adapter.PreviewRecipesAdapter
import ru.netology.nerecipe.databinding.FragmentFeedBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

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

                    viewModel.data.observe(viewLifecycleOwner) { recipes ->
                        binding.textBackground.visibility = View.GONE
                        adapter.submitList(recipes)
                    }
                    true
                }
                else -> false
            }
        }


/* НАЧАЛО drag and drop. Проблема - нет досутпа к мутабл дата,
есть только доступ к дата из вьюМодели - она неизменяемая, просто List

Пошла смотреть бд. Всё равно всё хранение поменяется, может это решит проблему!


        var touchCallBack:ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP
                            or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if(viewHolder.itemViewType != target.itemViewType)
                    return false

                val from = viewHolder.bindingAdapterPosition
                val to = target.bindingAdapterPosition

                val item = list.removeAt(from)
                list.add(to, item)

                recyclerView.adapter!!.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        }

 */

        return binding.root
    }


}