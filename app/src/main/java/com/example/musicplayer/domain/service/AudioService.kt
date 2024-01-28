package com.example.musicplayer.domain.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.musicplayer.domain.entity.AudioEntity

class AudioService : Service() {

	private var mediaPlayer: MediaPlayer? = null

	inner class MyBinder : Binder() {

		val service: AudioService = this@AudioService
	}

	override fun onCreate() {
		super.onCreate()
		mediaPlayer = MediaPlayer()
	}

	override fun onBind(intent: Intent): IBinder {
		return MyBinder()
	}

	override fun onDestroy() {
		stopAudioToService()
		mediaPlayer = null
		super.onDestroy()
	}

	fun startAudioToService(entity: AudioEntity, onDurationActual: (Int) -> Unit) {
		stopAudioToService()
		mediaPlayer?.reset()
		mediaPlayer?.setDataSource(entity.path)
		mediaPlayer?.prepare()
		mediaPlayer?.start()
		while (mediaPlayer?.isPlaying == true) {
			onDurationActual(mediaPlayer?.currentPosition ?: 0)
		}
		onDurationActual(0)
	}

	fun stopAudioToService() {
		mediaPlayer?.stop()
	}
}