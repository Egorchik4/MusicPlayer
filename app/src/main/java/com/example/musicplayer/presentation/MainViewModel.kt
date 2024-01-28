package com.example.musicplayer.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.domain.convertors.toTimeFormat
import com.example.musicplayer.domain.entity.AudioEntity
import com.example.musicplayer.domain.usecase.AudioPlayerUseCase
import com.example.musicplayer.domain.usecase.GetOfInSharedPreferencesUseCase
import com.example.musicplayer.domain.usecase.PutInSharedPreferencesUseCase
import com.example.musicplayer.presentation.state.AudioItemState
import com.example.musicplayer.presentation.state.AudioState
import com.example.musicplayer.ui.MainActivity.Companion.KEY_SHARED_PREFERENCES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val putInSharedPreferencesUseCase: PutInSharedPreferencesUseCase,
	private val getOfInSharedPreferencesUseCase: GetOfInSharedPreferencesUseCase,
	private val audioPlayerUseCase: AudioPlayerUseCase
) : ViewModel() {

	private val _musicMut = MutableLiveData<AudioState>()
	val musicLive: LiveData<AudioState> = _musicMut

	private var currentMusicPosition = 0
	private var musicListSize = 0
	private lateinit var musicListStop: List<AudioItemState>

	fun saveMusicFolder(pathAudio: String) {
		if (pathAudio != "null") {
			putInSharedPreferencesUseCase(KEY_SHARED_PREFERENCES, pathAudio)
			val list = audioPlayerUseCase.getAudioList(pathAudio)
			musicListStop = list
			musicListSize = list.size
			_musicMut.value = AudioState.Content(enableButton = false, list = list)
		} else {
			_musicMut.value = AudioState.Initial
		}
	}

	fun clickToItem(audioItemState: AudioItemState) {
		when (audioItemState) {
			is AudioItemState.PlayItem -> {
				stopAudio()
			}
			is AudioItemState.StopItem -> {
				val position = musicListStop.indexOf(audioItemState)
				playAudio(audioItemState.audioEntity, position)
			}
		}
	}

	private fun changeData(audioEntity: AudioEntity, position: Int) {
		val newList = ArrayList(musicListStop)
		if(audioEntity.actualDurationInt != 0){
			newList[position] = AudioItemState.PlayItem(audioEntity)
			_musicMut.postValue(
				AudioState.Content(enableButton = true, list = newList)
			)
		}else{
			_musicMut.postValue(
				AudioState.Content(enableButton = false, list = newList)
			)
		}
	}

	fun playAudio(audioEntity: AudioEntity = audioPlayerUseCase.getActualAudio(0), position: Int = 0) {
		Log.e("eee", "playAudio $audioEntity")
		viewModelScope.launch(Dispatchers.IO) {
			audioPlayerUseCase.playAudio(audioEntity) {
				Log.e("eee", "duration: $it")
				changeData(
					audioEntity = audioEntity.copy(
						actualDuration = it.toTimeFormat(),
						actualDurationInt = it
					),
					position = position
				)
			}
		}
	}

	fun stopAudio() {
		audioPlayerUseCase.stopAudio()
	}

	fun nextAudio() {
		stopAudio()
		currentMusicPosition += 1
		if (currentMusicPosition > musicListSize - 1) currentMusicPosition = 0
		playAudio(audioPlayerUseCase.getActualAudio(currentMusicPosition))
	}

	fun previousAudio() {
		stopAudio()
		currentMusicPosition -= 1
		if (currentMusicPosition < 0) currentMusicPosition = musicListSize - 1
		playAudio(audioPlayerUseCase.getActualAudio(currentMusicPosition))
	}

	init {
		saveMusicFolder(getOfInSharedPreferencesUseCase(KEY_SHARED_PREFERENCES)!!)
	}
}