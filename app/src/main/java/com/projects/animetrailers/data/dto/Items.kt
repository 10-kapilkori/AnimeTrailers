package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Items(
    @Json(name = "count")
    val count: Int,
    @Json(name = "per_page")
    val perPage: Int,
    @Json(name = "total")
    val total: Int
)