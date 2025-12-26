package com.projects.animetrailers.domain.repository

import androidx.paging.PagingData
import com.projects.animetrailers.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun getTopAnime(): Flow<PagingData<Anime>>
    suspend fun getAnimeById(animeId: Int): Anime?
}

