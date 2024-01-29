package com.example.musicplayer.domain.usecase

import android.content.SharedPreferences
import javax.inject.Inject

class GetOfInSharedPreferencesUseCase @Inject constructor(private val sharedPreferences: SharedPreferences) {

	operator fun invoke(key: String): String? {
		return sharedPreferences.getString(key, "")
	}
}