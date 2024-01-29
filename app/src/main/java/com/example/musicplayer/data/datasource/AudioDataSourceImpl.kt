package com.example.musicplayer.data.datasource

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.musicplayer.domain.convertors.toTimeFormat
import com.example.musicplayer.domain.entity.AudioEntity
import java.io.File
import javax.inject.Inject

class AudioDataSourceImpl @Inject constructor(private val context: Context, private val mmr: MediaMetadataRetriever) : AudioDataSource {

	override fun getMusicListFromDirectory(directoryPath: String): List<AudioEntity> {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			getAllAudioContentFromDirectoryForNewerApi(directoryPath.toUri())
		} else {
			getAllAudioContentFromDirectoryForOlderApi(directoryPath)
		}
		return audioList
	}

	override fun getActualMusic(position: Int): AudioEntity =
		audioList[position]

	private var audioList: MutableList<AudioEntity> = mutableListOf()

	private var selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

	private val projections = arrayOf(
		MediaStore.Audio.Media.DATA,
		MediaStore.Audio.Media.DURATION,
		MediaStore.Audio.Media.DISPLAY_NAME
	)

	private fun getAllAudioContentFromDirectoryForNewerApi(contentLocation: Uri): List<AudioEntity> {
		context.contentResolver.query(
			contentLocation, projections, selection,
			null,
			"LOWER (" + MediaStore.Audio.Media.TITLE + ") ASC"
		)!!.use { cursor ->
			if (cursor.moveToFirst()) {
				do {
					val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
					val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
					val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))

					audioList.add(
						AudioEntity(
							path = path,
							name = name,
							timeOfDuration = duration.toTimeFormat(),
							maxOfDurationInt = duration,
						)
					)
				} while (cursor.moveToNext())
			}
		}
		return audioList
	}

	private fun getAllAudioContentFromDirectoryForOlderApi(directoryPath: String): List<AudioEntity> {
		val directPath = directoryPath.toUri().toFile()
		File(directPath.path).listFiles()?.forEach {
			audioList.add(
				AudioEntity(
					path = it.path,
					name = it.name,
					timeOfDuration = getDurationOfAudio(it.absolutePath).toTimeFormat(),
					maxOfDurationInt = getDurationOfAudio(it.absolutePath)
				)
			)
		}
		return audioList
	}

	private fun getDurationOfAudio(pathAudio: String): Int {
		var duration = ""
		mmr.apply {
			setDataSource(pathAudio)
			duration = extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toString()
		}
		return Integer.parseInt(duration)
	}
}