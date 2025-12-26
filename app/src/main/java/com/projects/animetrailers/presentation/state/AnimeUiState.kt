package com.projects.animetrailers.presentation.state

import com.projects.animetrailers.domain.model.Anime

data class AnimeUiState(
    val isLoading: Boolean = false,
    val animeList: List<Anime> = emptyList(),
    val errorMessage: String? = null
)

