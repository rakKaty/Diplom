package ru.netology.nerecipe.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.databinding.FragmentNewRecipeBinding
import ru.netology.nerecipe.util.LongArg
import ru.netology.nerecipe.viewModel.RecipeViewModel

class NewRecipeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewRecipeBinding.inflate(inflater, container, false)

        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)


        val recipeId = arguments?.idArg

        if (recipeId != null) {
            viewModel.data.observe(viewLifecycleOwner) { list ->
                val recipe = list.find { it.id == recipeId } ?: run {
                    findNavController().navigateUp()
                    return@observe
                }
                recipe.let {
                    binding.editTextRecipeName.setText(it.recipeName)
                    binding.editTextRecipe.setText(it.content)
                    binding.editTextAuthorName.setText(it.authorName)
                    binding.editTextRecipePhoto.setText(it.photo)
                }
            }

        }


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


        binding.editTextRecipeName.requestFocus()
        binding.ok.setOnClickListener {

            val recipeName = binding.editTextRecipeName.text.toString()
            val content = binding.editTextRecipe.text.toString()
            val authorName = binding.editTextAuthorName.text.toString()
            val recipeCategory = chipIdToText(binding.chipGroupCategory.checkedChipId)
            val photo = binding.editTextRecipePhoto.text.toString()


            viewModel.onCreateNewRecipeNew(
                recipeName,
                content,
                authorName,
                recipeCategory,
                photo
            )
            findNavController().navigateUp()
        }
        return binding.root
    }


    companion object {
        var Bundle.idArg: Long? by LongArg
    }

}