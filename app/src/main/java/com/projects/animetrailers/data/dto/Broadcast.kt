package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Broadcast(
    @Json(name = "day")
    val day: String? = null,
    @Json(name = "string")
    val string: String? = null,
    @Json(name = "time")
    val time: String? = null,
    @Json(name = "timezone")
    val timezone: String? = null
)