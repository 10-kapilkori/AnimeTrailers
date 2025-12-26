package com.projects.animetrailers.data.datasource.remote

import com.projects.animetrailers.data.dto.AnimesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanApiService {
    @GET("v4/top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): AnimesDto
}

