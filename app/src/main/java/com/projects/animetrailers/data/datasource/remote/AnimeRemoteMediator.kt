package com.projects.animetrailers.data.datasource.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.projects.animetrailers.data.local.AnimeDatabase
import com.projects.animetrailers.data.local.AnimeEntity
import com.projects.animetrailers.data.local.RemoteKey
import com.projects.animetrailers.data.mapper.AnimeMapper
import com.projects.animetrailers.utils.NetworkMonitor
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AnimeRemoteMediator(
    private val apiService: JikanApiService,
    private val database: AnimeDatabase,
    private val networkMonitor: NetworkMonitor
) : RemoteMediator<Int, AnimeEntity>() {

    companion object {
        private const val TAG = "AnimeRemoteMediator"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnimeEntity>
    ): MediatorResult {
        return try {
            // Check network connectivity
            if (!networkMonitor.isOnline()) {
                Log.d(TAG, "No network connection, loading from cache")
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    database.withTransaction {
                        database.remoteKeyDao().clearRemoteKeys()
                        database.animeDao().clearAll()
                    }
                    1
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        database.remoteKeyDao().getRemoteKey()
                    }
                    if (remoteKey?.nextKey == null) {
                        1
                    } else {
                        remoteKey.nextKey
                    }
                }
            }

            Log.d(TAG, "Loading page: $page")

            val response = apiService.getTopAnime(page, state.config.pageSize)
            val animeList = response.data?.map { 
                AnimeMapper.run { it.toDomain().toEntity() }
            } ?: emptyList()

            val hasNextPage = response.pagination?.hasNextPage ?: false
            val nextKey = if (hasNextPage && animeList.isNotEmpty()) page + 1 else null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.animeDao().clearAll()
                    database.remoteKeyDao().clearRemoteKeys()
                }
                database.animeDao().insertAllAnime(animeList)
                database.remoteKeyDao().insertOrReplace(RemoteKey(nextKey = nextKey))
            }

            Log.d(TAG, "Page $page loaded: ${animeList.size} items, hasNextPage: $hasNextPage")

            MediatorResult.Success(endOfPaginationReached = !hasNextPage || animeList.isEmpty())
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error: ${e.message}", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}", e)
            MediatorResult.Error(e)
        }
    }
}

