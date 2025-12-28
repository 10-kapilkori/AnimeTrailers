package com.projects.animetrailers.data.datasource.local

import androidx.paging.PagingSource
import com.projects.animetrailers.data.local.AnimeDao
import com.projects.animetrailers.data.local.AnimeEntity
import javax.inject.Inject

interface AnimeLocalDataSource {
    fun getAllAnime(): PagingSource<Int, AnimeEntity>
    suspend fun getAnimeById(animeId: Int): AnimeEntity?
    suspend fun insertAnime(anime: AnimeEntity)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)
    suspend fun clearAll()
    suspend fun getCount(): Int
    suspend fun clearAndInsertAll(animeList: List<AnimeEntity>)
}

class AnimeLocalDataSourceImpl @Inject constructor(
    private val animeDao: AnimeDao
) : AnimeLocalDataSource {
    override fun getAllAnime(): PagingSource<Int, AnimeEntity> {
        return animeDao.getAllAnime()
    }

    override suspend fun getAnimeById(animeId: Int): AnimeEntity? {
        return animeDao.getAnimeById(animeId)
    }

    override suspend fun insertAnime(anime: AnimeEntity) {
        animeDao.insertAnime(anime)
    }

    override suspend fun insertAllAnime(animeList: List<AnimeEntity>) {
        animeDao.insertAllAnime(animeList)
    }

    override suspend fun clearAll() {
        animeDao.clearAll()
    }

    override suspend fun getCount(): Int {
        return animeDao.getCount()
    }

    override suspend fun clearAndInsertAll(animeList: List<AnimeEntity>) {
        animeDao.clearAndInsertAll(animeList)
    }
}

