package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesX(
    @Json(name = "image_url")
    val imageUrl: Any? = null,
    @Json(name = "large_image_url")
    val largeImageUrl: Any? = null,
    @Json(name = "maximum_image_url")
    val maximumImageUrl: Any? = null,
    @Json(name = "medium_image_url")
    val mediumImageUrl: Any? = null,
    @Json(name = "small_image_url")
    val smallImageUrl: Any? = null
)