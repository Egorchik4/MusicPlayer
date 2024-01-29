package com.example.musicplayer.domain.usecase

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class PutInSharedPreferencesUseCase @Inject constructor(private val sharedPreferences: SharedPreferences) {

	operator fun invoke(key: String, data: String) {
		sharedPreferences.edit {
			putString(key, data).apply()
		}
	}
}