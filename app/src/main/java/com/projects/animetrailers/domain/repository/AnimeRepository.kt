package com.projects.animetrailers.domain.repository

import com.projects.animetrailers.data.datasource.remote.ApiState
import com.projects.animetrailers.domain.model.Anime

interface AnimeRepository {
    suspend fun getTopAnime(page: Int = 1, limit: Int = 25): ApiState<List<Anime>>
}

