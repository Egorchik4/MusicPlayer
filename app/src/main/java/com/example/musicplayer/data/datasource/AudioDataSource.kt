package com.example.musicplayer.data.datasource

import com.example.musicplayer.domain.entity.AudioEntity

interface AudioDataSource {

	fun getMusicListFromDirectory(directoryPath: String): List<AudioEntity>

	fun getActualMusic(position: Int): AudioEntity
}