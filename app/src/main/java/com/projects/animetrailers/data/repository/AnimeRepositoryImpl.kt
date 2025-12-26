package com.projects.animetrailers.data.repository

import com.projects.animetrailers.data.datasource.remote.AnimeRemoteDataSource
import com.projects.animetrailers.data.datasource.remote.ApiState
import com.projects.animetrailers.data.mapper.AnimeMapper.toDomain
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.repository.AnimeRepository

class AnimeRepositoryImpl(
    private val remoteDataSource: AnimeRemoteDataSource
) : AnimeRepository {

    override suspend fun getTopAnime(page: Int, limit: Int): ApiState<List<Anime>> {
        return when (val result = remoteDataSource.getTopAnime(page, limit)) {
            is ApiState.Loading -> ApiState.Loading
            is ApiState.Success -> {
                val animeList = result.data.data.toDomain()
                ApiState.Success(animeList)
            }

            is ApiState.Error -> ApiState.Error(result.message, result.throwable)
        }
    }
}

