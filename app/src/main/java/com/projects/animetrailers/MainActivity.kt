package com.projects.animetrailers

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.projects.animetrailers.presentation.viewmodel.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private val viewModel: AnimeViewModel by viewModels()
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeAnimeData()
    }

    private fun observeAnimeData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when {
                    state.isLoading -> {
                        Log.d(TAG, "Loading anime data...")
                    }
                    state.errorMessage != null -> {
                        Log.e(TAG, "Error: ${state.errorMessage}")
                    }
                    state.animeList.isNotEmpty() -> {
                        Log.d(TAG, "Anime data received: ${state.animeList.size} items")
                        state.animeList.forEach { anime ->
                            Log.d(TAG, """
                                |Anime ID: ${anime.id}
                                |Title: ${anime.title}
                                |Episodes: ${anime.episodes}
                                |Rating: ${anime.rating}
                                |Genres: ${anime.genres.joinToString(", ")}
                                |Synopsis: ${anime.synopsis.take(100)}...
                                |Poster URL: ${anime.posterImageUrl}
                                |---
                            """.trimMargin())
                        }
                    }
                }
            }
        }
    }
}