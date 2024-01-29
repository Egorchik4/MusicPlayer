package com.example.musicplayer.presentation.state

sealed class AudioState {
	data object Initial : AudioState()

	data class Content(val enableButton: Boolean, val list: List<AudioItemState>) : AudioState()
}