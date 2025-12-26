package com.projects.animetrailers.data.datasource.remote

import com.projects.animetrailers.data.dto.AnimesDto
import java.io.IOException

class AnimeRemoteDataSourceImpl(
    private val apiService: JikanApiService
) : AnimeRemoteDataSource {

    override suspend fun getTopAnime(page: Int, limit: Int): ApiState<AnimesDto> {
        return try {
            val response = apiService.getTopAnime(page, limit)
            ApiState.Success(response)
        } catch (e: IOException) {
            ApiState.Error(
                message = "Network error: ${e.message}",
                throwable = e
            )
        } catch (e: Exception) {
            ApiState.Error(
                message = "Unexpected error: ${e.message}",
                throwable = e
            )
        }
    }
}

