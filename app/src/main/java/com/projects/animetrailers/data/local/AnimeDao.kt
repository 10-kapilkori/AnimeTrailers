package com.projects.animetrailers.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime ORDER BY rating DESC")
    fun getAllAnime(): PagingSource<Int, AnimeEntity>

    @Query("SELECT * FROM anime WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Int): AnimeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)

    @Query("DELETE FROM anime")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM anime")
    suspend fun getCount(): Int

    @Query("SELECT MAX(lastUpdated) FROM anime")
    suspend fun getLastUpdated(): Long?

    @Transaction
    suspend fun clearAndInsertAll(animeList: List<AnimeEntity>) {
        clearAll()
        insertAllAnime(animeList)
    }
}

