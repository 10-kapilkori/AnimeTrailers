package com.projects.animetrailers.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeDetailDto(
    @Json(name = "data")
    val data: Data? = null
)

