package ru.netology.nerecipe.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.NewRecipeFragment.Companion.textArg
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

        val viewHolder = RecipesAdapter.RecipeViewHolder(binding.postLayout, viewModel)


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

        viewModel.playVideo.observe(viewLifecycleOwner) { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        viewModel.navigateToPostContentScreen.observe(viewLifecycleOwner) { text ->
            findNavController().navigate(
                R.id.action_postFragment_to_newPostFragment,
                Bundle().apply { textArg = text }
            )
        }

        /* тоже самое, что выше, только через элвиса
               viewModel.data.observe(viewLifecycleOwner) { list ->
                   val post = list.find { it.id == arguments?.idArg }
                   if (list.contains(post)){
                       if (post != null) {
                           viewHolder.bind(post)
                       }
                   } else {
                       findNavController()
                           .navigateUp()
                   }
               }
              */


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