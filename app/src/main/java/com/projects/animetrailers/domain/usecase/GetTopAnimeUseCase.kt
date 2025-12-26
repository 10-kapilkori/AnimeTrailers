package com.projects.animetrailers.domain.usecase

import androidx.paging.PagingData
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetTopAnimeUseCase(
    private val repository: AnimeRepository
) {
    operator fun invoke(): Flow<PagingData<Anime>> {
        return repository.getTopAnime()
    }
}

