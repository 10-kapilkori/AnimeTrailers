package com.projects.animetrailers.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.projects.animetrailers.data.datasource.local.AnimeLocalDataSource
import com.projects.animetrailers.data.datasource.remote.AnimeRemoteMediator
import com.projects.animetrailers.data.datasource.remote.JikanApiService
import com.projects.animetrailers.data.local.AnimeDatabase
import com.projects.animetrailers.data.mapper.AnimeMapper
import com.projects.animetrailers.domain.model.Anime
import com.projects.animetrailers.domain.repository.AnimeRepository
import com.projects.animetrailers.utils.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class AnimeRepositoryImpl @Inject constructor(
    private val apiService: JikanApiService,
    private val database: AnimeDatabase,
    private val localDataSource: AnimeLocalDataSource,
    private val networkMonitor: NetworkMonitor
) : AnimeRepository {

    override fun getTopAnime(): Flow<PagingData<Anime>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = AnimeRemoteMediator(
                apiService = apiService,
                database = database,
                networkMonitor = networkMonitor
            ),
            pagingSourceFactory = {
                localDataSource.getAllAnime()
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                AnimeMapper.run { entity.toDomain() }
            }
        }
    }

    override suspend fun getAnimeById(animeId: Int): Anime? {
        return try {
            // Try to get from local database first
            val localAnime = localDataSource.getAnimeById(animeId)
            if (localAnime != null) {
                return AnimeMapper.run { localAnime.toDomain() }
            }

            // If not found locally and online, fetch from API
            if (networkMonitor.isOnline()) {
                val response = apiService.getAnimeById(animeId)
                val anime = response.data?.let { 
                    AnimeMapper.run { it.toDomain() }
                }
                
                // Save to local database
                anime?.let {
                    localDataSource.insertAnime(AnimeMapper.run { it.toEntity() })
                }
                
                anime
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

