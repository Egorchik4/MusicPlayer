package com.example.musicplayer.domain.usecase

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.musicplayer.domain.entity.AudioEntity
import com.example.musicplayer.domain.repository.AudioRepository
import com.example.musicplayer.domain.service.AudioService
import com.example.musicplayer.presentation.state.AudioItemState
import javax.inject.Inject

class AudioPlayerUseCase @Inject constructor(private val context: Context, private val audioRepository: AudioRepository) {

	var bound = false
	var myService: AudioService? = null

	fun getAudioList(pathOfFile: String): List<AudioItemState> =
		audioRepository.getMusicListFromDirectory(pathOfFile)

	fun getActualAudio(position: Int): AudioEntity {
		return audioRepository.getActualMusic(position)
	}

	fun playAudio(audioEntity: AudioEntity, onDurationActual: (Int) -> Unit) {
		if (bound) {
			myService?.startAudioToService(audioEntity, onDurationActual)
		} else {
			startService()
		}
	}

	fun stopAudio() {
		myService?.stopAudioToService()
	}

	fun startService() {
		val intent = Intent(context, AudioService::class.java)
		context.startService(intent)
		context.bindService(intent, sConn, 0)
	}

	fun destroyService() {
		if (!bound) return
		val intent = Intent(context, AudioService::class.java)
		context.unbindService(sConn)
		context.stopService(intent)
		bound = false
	}

	private val sConn = object : ServiceConnection {
		override fun onServiceConnected(name: ComponentName, binder: IBinder) {
			bound = true
			myService = (binder as? AudioService.MyBinder)?.service
		}

		override fun onServiceDisconnected(name: ComponentName) {
			bound = false
		}
	}
}