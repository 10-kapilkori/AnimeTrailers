package com.projects.animetrailers.data.mapper

import com.projects.animetrailers.data.dto.Data
import com.projects.animetrailers.domain.model.Anime

object AnimeMapper {
    fun Data.toDomain(): Anime {
        return Anime(
            id = malId,
            title = title,
            synopsis = synopsis.ifEmpty { "No synopsis available." },
            genres = genres.map { it.name },
            mainCast = emptyList(), // Jikan API top anime endpoint doesn't provide cast information
            episodes = episodes,
            rating = score,
            posterImageUrl = images.jpg.imageUrl.ifEmpty { images.jpg.largeImageUrl }
        )
    }

    fun List<Data>.toDomain(): List<Anime> {
        return map { it.toDomain() }
    }
}

