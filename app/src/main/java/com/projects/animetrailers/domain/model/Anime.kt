package com.projects.animetrailers.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val synopsis: String,
    val genres: List<String>,
    val mainCast: List<String>, // Note: Jikan API top anime endpoint doesn't provide cast, will be empty
    val episodes: Int,
    val rating: Double,
    val posterImageUrl: String
)

