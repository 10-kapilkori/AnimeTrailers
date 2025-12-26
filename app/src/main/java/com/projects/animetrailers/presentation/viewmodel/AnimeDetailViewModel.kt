package com.projects.animetrailers.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = savedStateHandle.get<Int>("animeId") ?: 0

    private val _anime = MutableStateFlow<Anime?>(null)
    val anime: StateFlow<Anime?> = _anime.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAnimeDetails()
    }

    private fun loadAnimeDetails() {
        if (animeId == 0) {
            _error.value = "Invalid anime ID"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val animeData = repository.getAnimeById(animeId)
                if (animeData != null) {
                    _anime.value = animeData
                } else {
                    _error.value = "Failed to load anime details"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

