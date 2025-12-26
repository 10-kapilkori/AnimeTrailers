package com.projects.animetrailers.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.projects.animetrailers.R
import com.projects.animetrailers.databinding.ItemAnimeBinding
import com.projects.animetrailers.domain.model.Anime

class AnimeAdapter(
    private val onItemClick: (Anime) -> Unit
) : PagingDataAdapter<Anime, AnimeAdapter.AnimeViewHolder>(AnimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = getItem(position)
        anime?.let {
            holder.bind(it)
        }
    }

    inner class AnimeViewHolder(
        private val binding: ItemAnimeBinding,
        private val onItemClick: (Anime) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.apply {
                textViewTitle.text = anime.title
                textViewEpisodes.text = "Episodes: ${anime.episodes}"
                textViewRating.text = "Rating: ${anime.rating}"
                textViewGenres.text = anime.genres.joinToString(", ")

                // Load image using Glide
                Glide.with(imageViewPoster.context)
                    .load(anime.posterImageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(imageViewPoster)

                // Set click listener
                root.setOnClickListener {
                    onItemClick(anime)
                }
            }
        }
    }

    class AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }
}

