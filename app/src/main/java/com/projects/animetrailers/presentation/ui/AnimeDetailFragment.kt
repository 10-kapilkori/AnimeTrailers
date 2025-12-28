package com.projects.animetrailers.presentation.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.projects.animetrailers.R
import com.projects.animetrailers.databinding.FragmentAnimeDetailBinding
import com.projects.animetrailers.presentation.viewmodel.AnimeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimeDetailFragment : Fragment(R.layout.fragment_anime_detail) {

    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeDetailViewModel by viewModels()

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnimeDetailBinding.bind(view)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    binding.textViewError.visibility = View.VISIBLE
                    binding.textViewError.text = error
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                } else {
                    binding.textViewError.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.anime.collect { anime ->
                anime?.let {
                    displayAnimeDetails(it)
                }
            }
        }
    }

    private fun displayAnimeDetails(anime: com.projects.animetrailers.domain.model.Anime) {
        binding.apply {
            textViewTitle.text = anime.title
            textViewRating.text = "${anime.rating}"
            textViewEpisodes.text = "${anime.episodes} Episodes"
            textViewSynopsis.text = anime.synopsis

            // Genres
            if (anime.genres.isNotEmpty()) {
                chipGenre1.text = anime.genres[0]
                chipGenre1.visibility = View.VISIBLE
            } else {
                chipGenre1.visibility = View.GONE
            }

            if (anime.genres.size > 1) {
                chipGenre2.text = anime.genres[1]
                chipGenre2.visibility = View.VISIBLE
            } else {
                chipGenre2.visibility = View.GONE
            }

            // Year (Placeholder - hide for now as we don't have data)
            textViewYear.visibility = View.GONE

            // Back Button
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            // Handle trailer or poster image
            showPosterImage(anime.posterImageUrl)

            if (anime.trailerUrl != null && anime.trailerUrl.isNotEmpty()) {
                buttonPlay.visibility = View.VISIBLE
                buttonPlay.setOnClickListener {
                    setupVideoPlayer(anime.trailerUrl)
                }
            } else {
                buttonPlay.visibility = View.GONE
            }
        }
    }

    private fun setupVideoPlayer(trailerUrl: String) {
        binding.apply {
            imageViewPoster.visibility = View.GONE
            buttonPlay.visibility = View.GONE

            // Try to extract YouTube ID
            val youtubeId = extractYouTubeId(trailerUrl)

            if (youtubeId != null) {
                setupYouTubePlayer(youtubeId, trailerUrl)
            } else if (trailerUrl.startsWith("http")) {
                // Try ExoPlayer for direct video URLs
                val videoUri = trailerUrl.toUri()
                initializePlayer(videoUri)
            } else {
                showPosterImage("")
            }
        }
    }

    private fun extractYouTubeId(url: String): String? {
        return try {
            when {
                url.contains("youtube.com/watch?v=") -> {
                    url.substringAfter("v=").substringBefore("&")
                }

                url.contains("youtu.be/") -> {
                    url.substringAfter("youtu.be/").substringBefore("?")
                }

                url.contains("/embed/") -> {
                    url.substringAfter("/embed/").substringBefore("?")
                }

                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun setupYouTubePlayer(videoId: String, trailerUrl: String) {
        binding.apply {
            youtubePlayerView.visibility = View.VISIBLE
            playerView.visibility = View.GONE

            lifecycle.addObserver(youtubePlayerView)

            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError
                ) {
                    super.onError(youTubePlayer, error)
                    Toast.makeText(
                        requireContext(),
                        "Error in player, opening externally...",
                        Toast.LENGTH_SHORT
                    ).show()
                    openExternalVideo(trailerUrl)
                }
            })
        }
    }

    private fun initializePlayer(videoUri: Uri) {
        releasePlayer()

        binding.apply {
            youtubePlayerView.visibility = View.GONE
            playerView.visibility = View.VISIBLE
        }

        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.seekTo(currentWindow, playbackPosition)

            exoPlayer.addListener(object : androidx.media3.common.Player.Listener {
                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(
                        requireContext(),
                        "Error playing video, opening externally...",
                        Toast.LENGTH_SHORT
                    ).show()
                    openExternalVideo(videoUri.toString())
                }
            })

            exoPlayer.prepare()
        }
    }

    private fun openExternalVideo(url: String) {
        val videoId = extractYouTubeId(url)

        try {
            if (videoId != null) {
                try {
                    val appIntent = Intent(
                        Intent.ACTION_VIEW,
                        "vnd.youtube:$videoId".toUri()
                    )
                    startActivity(appIntent)
                    return
                } catch (e: ActivityNotFoundException) {
                    // YouTube app not installed, disable this block to fall through to web intent
                }
            }

            val webIntent =
                Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(webIntent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Could not open video link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPosterImage(posterUrl: String) {
        binding.apply {
            playerView.visibility = View.GONE
            youtubePlayerView.visibility = View.GONE
            imageViewPoster.visibility = View.VISIBLE

            if (posterUrl.isNotEmpty()) {
                Glide.with(imageViewPoster.context)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(imageViewPoster)
            }
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentWindow = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    override fun onResume() {
        super.onResume()
        player?.let {
            if (it.playWhenReady) {
                it.play()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        binding.youtubePlayerView.release()
        _binding = null
    }
}
