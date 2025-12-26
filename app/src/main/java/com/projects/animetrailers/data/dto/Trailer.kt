package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trailer(
    @Json(name = "embed_url")
    val embedUrl: String? = "",
    @Json(name = "images")
    val images: ImagesX? = ImagesX(),
    @Json(name = "url")
    val url: Any? = Any(),
    @Json(name = "youtube_id")
    val youtubeId: Any? = Any()
)