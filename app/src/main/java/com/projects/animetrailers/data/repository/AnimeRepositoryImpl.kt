package com.projects.animetrailers.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projects.animetrailers.data.datasource.remote.AnimePagingSource
import com.projects.animetrailers.data.datasource.remote.JikanApiService
import com.projects.animetrailers.data.mapper.AnimeMapper.toDomain
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apiService: JikanApiService
) : AnimeRepository {

    override fun getTopAnime(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {
                AnimePagingSource(apiService)
            }
        ).flow
    }

    override suspend fun getAnimeById(animeId: Int): Anime? {
        return try {
            val response = apiService.getAnimeById(animeId)
            response.data?.toDomain()
        } catch (e: Exception) {
            null
        }
    }
}

