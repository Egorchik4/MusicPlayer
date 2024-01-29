package com.example.musicplayer.presentation.state

import com.example.musicplayer.domain.entity.AudioEntity

sealed class AudioItemState {
	data class StopItem(var audioEntity: AudioEntity) : AudioItemState()

	data class PlayItem(var audioEntity: AudioEntity) : AudioItemState()
}