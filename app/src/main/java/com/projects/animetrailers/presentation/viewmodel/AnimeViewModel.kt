package com.projects.animetrailers.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.animetrailers.data.datasource.remote.ApiState
import com.projects.animetrailers.domain.usecase.GetTopAnimeUseCase
import com.projects.animetrailers.presentation.state.AnimeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeUiState())
    val uiState: StateFlow<AnimeUiState> = _uiState.asStateFlow()

    init {
        loadTopAnime()
    }

    fun loadTopAnime(page: Int = 1, limit: Int = 25) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getTopAnimeUseCase(page, limit)) {
                is ApiState.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is ApiState.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            animeList = result.data,
                            errorMessage = null
                        )
                    }
                }
                is ApiState.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            animeList = if (page == 1) emptyList() else it.animeList
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        loadTopAnime()
    }
}

