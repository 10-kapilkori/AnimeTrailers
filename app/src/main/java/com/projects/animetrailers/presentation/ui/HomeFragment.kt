package com.projects.animetrailers.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.animetrailers.R
import com.projects.animetrailers.databinding.FragmentHomeBinding
import com.projects.animetrailers.presentation.viewmodel.AnimeViewModel
import com.projects.animetrailers.utils.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeViewModel by viewModels()
    private lateinit var animeAdapter: AnimeAdapter
    private var loadingDialog: Dialog? = null
    private var internetRequiredDialog: AlertDialog? = null
    private var hasShownInternetDialog = false

    @Inject
    lateinit var networkMonitor: NetworkMonitor

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
        observeLoadState()
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

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                animeAdapter.loadStateFlow,
                networkMonitor.observeNetworkStatus()
            ) { loadState, isOnline ->
                Pair(loadState, isOnline)
            }.collectLatest { (loadState, isOnline) ->
                val isLoading = loadState.refresh is LoadState.Loading
                val hasError = loadState.refresh is LoadState.Error
                val isEmpty = animeAdapter.itemCount == 0
                
                // Show progress dialog on initial load when online
                if (isLoading && isEmpty && isOnline) {
                    showLoadingDialog()
                    hasShownInternetDialog = false // Reset flag when loading starts
                } else {
                    dismissLoadingDialog()
                }
                
                // Show internet required dialog on first launch when offline and no cached data
                if (!isLoading && isEmpty && !isOnline && !hasShownInternetDialog) {
                    showInternetRequiredDialog()
                } else if (!isEmpty) {
                    // Dismiss dialog only when data is available (after successful retry)
                    dismissInternetRequiredDialog()
                }
            }
        }
    }

    private fun showLoadingDialog() {
        if (loadingDialog == null || !loadingDialog!!.isShowing) {
            val progressBar = ProgressBar(requireContext()).apply {
                isIndeterminate = true
            }
            
            loadingDialog = AlertDialog.Builder(requireContext())
                .setView(progressBar)
                .setCancelable(false)
                .create()
            
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun showInternetRequiredDialog() {
        if (internetRequiredDialog == null || !internetRequiredDialog!!.isShowing) {
            internetRequiredDialog = AlertDialog.Builder(requireContext())
                .setTitle("Internet Required")
                .setMessage("No Internet connection. Please check you internet connection")
                .setPositiveButton("Retry") { _, _ ->
                    hasShownInternetDialog = false // Allow showing again if retry fails
                    // Trigger refresh to make a network call
                    viewLifecycleOwner.lifecycleScope.launch {
                        animeAdapter.refresh()
                    }
                }
                .setCancelable(false)
                .create()
            
            internetRequiredDialog?.show()
            hasShownInternetDialog = true
        }
    }

    private fun dismissInternetRequiredDialog() {
        internetRequiredDialog?.dismiss()
        internetRequiredDialog = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoadingDialog()
        dismissInternetRequiredDialog()
        _binding = null
    }
}

