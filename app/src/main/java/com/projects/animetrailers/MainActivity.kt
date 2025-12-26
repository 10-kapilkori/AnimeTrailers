package com.projects.animetrailers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projects.animetrailers.databinding.ActivityMainBinding
import com.projects.animetrailers.presentation.ui.AnimeAdapter
import com.projects.animetrailers.presentation.ui.AnimeLoadStateAdapter
import com.projects.animetrailers.presentation.viewmodel.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: AnimeViewModel by viewModels()
    private lateinit var animeAdapter: AnimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeAnimeData()
    }

    private fun setupRecyclerView() {
        animeAdapter = AnimeAdapter()
        
        val footerAdapter = AnimeLoadStateAdapter {
            animeAdapter.retry()
        }
        
        binding.recyclerViewAnime.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = animeAdapter.withLoadStateFooter(footerAdapter)
        }
    }

    private fun observeAnimeData() {
        lifecycleScope.launch {
            viewModel.animePagingFlow.collectLatest { pagingData ->
                animeAdapter.submitData(pagingData)
            }
        }
    }
}