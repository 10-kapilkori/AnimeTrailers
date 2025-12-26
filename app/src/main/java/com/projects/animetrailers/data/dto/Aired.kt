package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Aired(
    @Json(name = "from")
    val from: String? = null,
    @Json(name = "prop")
    val prop: Prop? = null,
    @Json(name = "string")
    val string: String? = null,
    @Json(name = "to")
    val to: String? = null
)