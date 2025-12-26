package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Prop(
    @Json(name = "from")
    val from: From? = null,
    @Json(name = "to")
    val to: To? = null
)