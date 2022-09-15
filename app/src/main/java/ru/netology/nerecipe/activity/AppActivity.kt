package ru.netology.nerecipe.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nerecipe.R
import ru.netology.nerecipe.activity.RecipeFragment.Companion.idArg
import ru.netology.nerecipe.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val intent = intent ?: return
        if (intent.action != Intent.ACTION_SEND) return

        val recipeId = intent.getLongExtra(Intent.EXTRA_TEXT, 0L)

        val recipeText = intent.getStringExtra(Intent.EXTRA_TEXT)



        if (recipeText.isNullOrBlank()) {
            Snackbar.make(
                binding.root,
                "Присланный текст пустой",
                Snackbar.LENGTH_INDEFINITE
            ).setAction(android.R.string.ok) {
                finish()
            }.show()
        }


        val fragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        fragment.navController.navigate(
            R.id.action_feedFragment_to_newRecipeFragment,
            Bundle().apply { idArg = recipeId }
        )


        fragment.navController.navigate(
            R.id.action_feedFragment_to_recipeFragment,
            Bundle().apply { idArg = recipeId }
        )


    }




}



