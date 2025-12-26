package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "has_next_page")
    val hasNextPage: Boolean,
    @Json(name = "items")
    val items: Items,
    @Json(name = "last_visible_page")
    val lastVisiblePage: Int
)