package com.projects.animetrailers.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val synopsis: String,
    val genres: List<String>,
    val episodes: Int,
    val rating: Double,
    val posterImageUrl: String,
    val trailerUrl: String? = null, // YouTube video URL or embed URL for trailer
    val approved: Boolean = false
)

