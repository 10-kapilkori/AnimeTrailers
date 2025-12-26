package com.projects.animetrailers.data.datasource.remote

import com.projects.animetrailers.data.dto.AnimesDto

interface AnimeRemoteDataSource {
    suspend fun getTopAnime(page: Int = 1, limit: Int = 25): ApiState<AnimesDto>
}

