package com.example.musicplayer.data.repository

import com.example.musicplayer.data.datasource.AudioDataSource
import com.example.musicplayer.domain.convertors.toAudioItemStateList
import com.example.musicplayer.domain.entity.AudioEntity
import com.example.musicplayer.domain.repository.AudioRepository
import com.example.musicplayer.presentation.state.AudioItemState
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(private val dataSource: AudioDataSource) : AudioRepository {

	override fun getMusicListFromDirectory(directoryPath: String): List<AudioItemState> =
		dataSource.getMusicListFromDirectory(directoryPath).toAudioItemStateList()

	override fun getActualMusic(position: Int): AudioEntity =
		dataSource.getActualMusic(position)
}