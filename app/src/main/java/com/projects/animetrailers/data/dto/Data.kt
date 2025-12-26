package com.projects.animetrailers.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "aired")
    val aired: Aired? = Aired(),
    @Json(name = "airing")
    val airing: Boolean? = false,
    @Json(name = "approved")
    val approved: Boolean? = false,
    @Json(name = "background")
    val background: String? = "",
    @Json(name = "broadcast")
    val broadcast: Broadcast? = Broadcast(),
    @Json(name = "demographics")
    val demographics: List<Any?>? = listOf(),
    @Json(name = "duration")
    val duration: String? = "",
    @Json(name = "episodes")
    val episodes: Int? = 0,
    @Json(name = "explicit_genres")
    val explicitGenres: List<Any?>? = listOf(),
    @Json(name = "favorites")
    val favorites: Int? = 0,
    @Json(name = "genres")
    val genres: List<Genre>? = listOf(),
    @Json(name = "images")
    val images: Images? = Images(),
    @Json(name = "licensors")
    val licensors: List<Licensor>? = listOf(),
    @Json(name = "mal_id")
    val malId: Int? = 0,
    @Json(name = "members")
    val members: Int? = 0,
    @Json(name = "popularity")
    val popularity: Int? = 0,
    @Json(name = "producers")
    val producers: List<Producer>? = listOf(),
    @Json(name = "rank")
    val rank: Int? = 0,
    @Json(name = "rating")
    val rating: String? = "",
    @Json(name = "score")
    val score: Double? = 0.0,
    @Json(name = "scored_by")
    val scoredBy: Int? = 0,
    @Json(name = "season")
    val season: String? = "",
    @Json(name = "source")
    val source: String? = "",
    @Json(name = "status")
    val status: String? = "",
    @Json(name = "studios")
    val studios: List<Studio>? = listOf(),
    @Json(name = "synopsis")
    val synopsis: String? = "",
    @Json(name = "themes")
    val themes: List<Theme>? = listOf(),
    @Json(name = "title")
    val title: String? = "",
    @Json(name = "title_english")
    val titleEnglish: String? = "",
    @Json(name = "title_japanese")
    val titleJapanese: String? = "",
    @Json(name = "title_synonyms")
    val titleSynonyms: List<Any?>? = listOf(),
    @Json(name = "titles")
    val titles: List<Title>? = listOf(),
    @Json(name = "trailer")
    val trailer: Trailer? = Trailer(),
    @Json(name = "type")
    val type: String? = "",
    @Json(name = "url")
    val url: String? = "",
    @Json(name = "year")
    val year: Int? = 0
)