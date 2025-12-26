package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Items(
    @Json(name = "count")
    val count: Int? = null,
    @Json(name = "per_page")
    val perPage: Int? = null,
    @Json(name = "total")
    val total: Int? = null
)