package com.projects.animetrailers.domain.usecase

import com.projects.animetrailers.data.datasource.remote.ApiState
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.repository.AnimeRepository

class GetTopAnimeUseCase(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        limit: Int = 25
    ): ApiState<List<Anime>> {
        return repository.getTopAnime(page, limit)
    }
}

