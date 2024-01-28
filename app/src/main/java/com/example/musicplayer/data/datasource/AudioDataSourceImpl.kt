package com.example.musicplayer.data.datasource

import android.media.MediaMetadataRetriever
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.musicplayer.domain.convertors.toTimeFormat
import com.example.musicplayer.domain.entity.AudioEntity
import java.io.File
import javax.inject.Inject

class AudioDataSourceImpl @Inject constructor(private val mmr: MediaMetadataRetriever) : AudioDataSource {

	private var audioList: MutableList<AudioEntity> = mutableListOf()

	override fun getMusicListFromDirectory(directoryPath: String): List<AudioEntity> {
		val directPath = directoryPath.toUri().toFile().parent
		if (directPath != null) {
			File(directPath).listFiles()?.forEach {
				audioList.add(
					AudioEntity(
						path = it.path,
						name = it.name,
						timeOfDuration = getDurationOfAudio(it.absolutePath).toTimeFormat(),
						maxOfDurationInt = getDurationOfAudio(it.absolutePath)
					)
				)
			}
		}
		return audioList
	}

	override fun getActualMusic(position: Int): AudioEntity =
		audioList[position]

	private fun getDurationOfAudio(pathAudio: String): Int {
		var duration = ""
		mmr.apply {
			setDataSource(pathAudio)
			duration = extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toString()
		}
		return Integer.parseInt(duration)
	}
}