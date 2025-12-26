package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class To(
    @Json(name = "day")
    val day: Int? = null,
    @Json(name = "month")
    val month: Int? = null,
    @Json(name = "year")
    val year: Int? = null
)