package com.projects.animetrailers.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase
) : ViewModel() {

    val animePagingFlow: Flow<PagingData<Anime>> = getTopAnimeUseCase()
        .cachedIn(viewModelScope)
}

