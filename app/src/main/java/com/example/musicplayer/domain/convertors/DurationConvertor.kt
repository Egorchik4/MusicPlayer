package com.example.musicplayer.domain.convertors

fun Int.toTimeFormat(): String {
	val minutes: Int = this / 60000
	val second: Int = (this % 60000) / 1000
	return "$minutes:$second"
}