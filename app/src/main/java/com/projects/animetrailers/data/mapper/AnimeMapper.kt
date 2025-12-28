package com.projects.animetrailers.data.mapper

import com.projects.animetrailers.data.dto.Data
import com.projects.animetrailers.data.local.AnimeEntity
import com.projects.animetrailers.domain.model.Anime

object AnimeMapper {
    fun AnimeEntity.toDomain(): Anime {
        return Anime(
            id = id,
            title = title,
            synopsis = synopsis,
            genres = genres,
            episodes = episodes,
            rating = rating,
            posterImageUrl = posterImageUrl,
            trailerUrl = trailerUrl,
            approved = approved
        )
    }

    fun Anime.toEntity(): AnimeEntity {
        return AnimeEntity(
            id = id,
            title = title,
            synopsis = synopsis,
            genres = genres,
            episodes = episodes,
            rating = rating,
            posterImageUrl = posterImageUrl,
            trailerUrl = trailerUrl,
            approved = approved,
            lastUpdated = System.currentTimeMillis()
        )
    }

    fun Data.toDomain(): Anime {
        // Handle nullable image URL
        val imageUrl = images?.jpg?.imageUrl?.takeIf { it.isNotEmpty() }
            ?: images?.jpg?.largeImageUrl?.takeIf { it.isNotEmpty() }
            ?: ""

        // Handle nullable title
        val animeTitle = title ?: ""

        // Handle nullable synopsis
        val animeSynopsis = synopsis?.takeIf { it.isNotEmpty() } ?: "No synopsis available."

        // Handle nullable genres list
        val animeGenres =
            genres?.mapNotNull { it.name?.takeIf { name -> name.isNotEmpty() } } ?: emptyList()

        // Handle nullable episodes (default to 0 if null)
        val animeEpisodes = episodes ?: 0

        // Handle nullable score (default to 0.0 if null)
        val animeRating = score ?: 0.0

        // Handle nullable malId (default to 0 if null)
        val animeId = malId ?: 0

        // Handle trailer URL - prefer embed_url, fallback to url, then youtube_id
        val trailerUrl = when {
            trailer?.embedUrl != null && trailer.embedUrl.isNotEmpty() -> trailer.embedUrl
            trailer?.url != null && trailer.url.toString() != "null" -> {
                val urlStr = trailer.url.toString()
                if (urlStr.startsWith("http")) urlStr else null
            }

            trailer?.youtubeId != null && trailer.youtubeId.toString() != "null" -> {
                val youtubeId = trailer.youtubeId.toString()
                "https://www.youtube.com/watch?v=$youtubeId"
            }

            else -> null
        }

        return Anime(
            id = animeId,
            title = animeTitle,
            synopsis = animeSynopsis,
            genres = animeGenres,
            episodes = animeEpisodes,
            rating = animeRating,
            posterImageUrl = imageUrl,
            trailerUrl = trailerUrl,
            approved = approved ?: false
        )
    }
}

