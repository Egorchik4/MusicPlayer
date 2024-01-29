package com.example.musicplayer.domain.repository

import com.example.musicplayer.domain.entity.AudioEntity
import com.example.musicplayer.presentation.state.AudioItemState

interface AudioRepository {

	fun getMusicListFromDirectory(directoryPath: String): List<AudioItemState>

	fun getActualMusic(position: Int): AudioEntity
}