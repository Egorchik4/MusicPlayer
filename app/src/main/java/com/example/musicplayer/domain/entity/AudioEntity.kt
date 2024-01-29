package com.example.musicplayer.domain.entity

data class AudioEntity(
	var path: String,
	var name: String,
	var actualDuration: String = "",
	var timeOfDuration: String,
	var maxOfDurationInt: Int,
	var actualDurationInt: Int = 0
)