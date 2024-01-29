package com.example.musicplayer.domain.convertors

import com.example.musicplayer.domain.entity.AudioEntity
import com.example.musicplayer.presentation.state.AudioItemState

fun List<AudioEntity>.toAudioItemStateList(): List<AudioItemState> =
	map(AudioEntity::toAudioItemState)

fun AudioEntity.toAudioItemState(): AudioItemState =
	AudioItemState.StopItem(this)