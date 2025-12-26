package com.projects.animetrailers.data.datasource.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.projects.animetrailers.data.dto.AnimesDto
import com.projects.animetrailers.data.mapper.AnimeMapper.toDomain
import com.projects.animetrailers.domain.model.Anime
import java.io.IOException

class AnimePagingSource(
    private val apiService: JikanApiService
) : PagingSource<Int, Anime>() {

    companion object {
        private const val TAG = "AnimePagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize.coerceAtMost(25) // Jikan API limit is 25 per page

            Log.d(TAG, "Loading page: $page, pageSize: $pageSize")

            val response: AnimesDto = apiService.getTopAnime(page, pageSize)
            
            // Handle nullable data list
            val animeList = response.data?.map { it.toDomain() } ?: emptyList()

            Log.d(TAG, "Page $page loaded: ${animeList.size} items")
            
            // Handle nullable pagination
            val pagination = response.pagination
            val hasNextPage = pagination?.hasNextPage ?: false
            val currentPage = pagination?.currentPage ?: page
            val lastVisiblePage = pagination?.lastVisiblePage ?: page
            
            Log.d(
                TAG,
                "Pagination - hasNextPage: $hasNextPage, currentPage: $currentPage, lastVisiblePage: $lastVisiblePage"
            )

            // Check if there are more pages by comparing current page with last visible page
            val nextKey =
                if (hasNextPage && page < lastVisiblePage) {
                    page + 1
                } else {
                    null
                }

            Log.d(TAG, "Next key: $nextKey")

            LoadResult.Page(
                data = animeList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Log.e(TAG, "Network error loading page ${params.key}: ${e.message}", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading page ${params.key}: ${e.message}", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

