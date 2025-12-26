package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimesDto(
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "pagination")
    val pagination: Pagination
)