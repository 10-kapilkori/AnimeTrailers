package com.projects.animetrailers.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.animetrailers.R
import com.projects.animetrailers.databinding.FragmentHomeBinding
import com.projects.animetrailers.presentation.viewmodel.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeViewModel by viewModels()
    private lateinit var animeAdapter: AnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeAnimeData()
    }

    private fun setupRecyclerView() {
        animeAdapter = AnimeAdapter { anime ->
            // Navigate to detail screen
            val bundle = Bundle().apply {
                putInt("animeId", anime.id)
            }
            findNavController().navigate(R.id.animeDetailFragment, bundle)
        }

        val footerAdapter = AnimeLoadStateAdapter {
            animeAdapter.retry()
        }

        binding.recyclerViewAnime.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = animeAdapter.withLoadStateFooter(footerAdapter)
        }
    }

    private fun observeAnimeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.animePagingFlow.collectLatest { pagingData ->
                animeAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

