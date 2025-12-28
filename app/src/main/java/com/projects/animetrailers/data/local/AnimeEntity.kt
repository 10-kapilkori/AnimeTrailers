package com.projects.animetrailers.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "anime")
@TypeConverters(Converters::class)
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val synopsis: String,
    val genres: List<String>,
    val episodes: Int,
    val rating: Double,
    val posterImageUrl: String,
    val trailerUrl: String?,
    val approved: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

