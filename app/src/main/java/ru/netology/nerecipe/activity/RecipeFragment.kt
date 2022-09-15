package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.RecipesAdapter
import ru.netology.nerecipe.databinding.FragmentRecipeBinding
import ru.netology.nerecipe.util.LongArg
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRecipeBinding.inflate(inflater, container, false)

        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val viewHolder = RecipesAdapter.RecipeViewHolder(binding.recipeLayout, viewModel)


        viewModel.shareRecipeContent.observe(viewLifecycleOwner) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.content)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }


        viewModel.navigateToRecipeAddOrEditScreen.observe(viewLifecycleOwner) { recipeId ->
            findNavController().navigate(
                R.id.action_recipeFragment_to_newRecipeFragment,
                Bundle().apply { idArg = recipeId }
            )
        }


        viewModel.data.observe(viewLifecycleOwner) { list ->
            val post = list.find { it.id == arguments?.idArg } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }
        return binding.root
    }

    companion object {
        var Bundle.idArg: Long? by LongArg
    }
}