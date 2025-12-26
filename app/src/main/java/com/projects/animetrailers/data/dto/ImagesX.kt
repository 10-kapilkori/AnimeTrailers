package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesX(
    @Json(name = "image_url")
    val imageUrl: String? = null,
    @Json(name = "large_image_url")
    val largeImageUrl: String? = null,
    @Json(name = "maximum_image_url")
    val maximumImageUrl: String? = null,
    @Json(name = "medium_image_url")
    val mediumImageUrl: String? = null,
    @Json(name = "small_image_url")
    val smallImageUrl: String? = null
)