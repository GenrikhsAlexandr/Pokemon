package com.aleksandrgenrikhs.pokemon.utils

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri

object PokemonMediaPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun initPlayer(application: Application, url: String): MediaPlayer? {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(application, Uri.parse(url))
        }
        return mediaPlayer
    }

    fun play() {
        mediaPlayer?.start()
    }

    fun destroyPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}