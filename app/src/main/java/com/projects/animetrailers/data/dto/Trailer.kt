package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trailer(
    @Json(name = "embed_url")
    val embedUrl: String? = null,
    @Json(name = "images")
    val images: ImagesX? = null,
    @Json(name = "url")
    val url: String? = null,
    @Json(name = "youtube_id")
    val youtubeId: String? = null
)