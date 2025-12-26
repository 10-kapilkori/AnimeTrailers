package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Title(
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String
)