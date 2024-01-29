package com.example.musicplayer.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaMetadataRetriever
import com.example.musicplayer.data.datasource.AudioDataSource
import com.example.musicplayer.data.datasource.AudioDataSourceImpl
import com.example.musicplayer.data.repository.AudioRepositoryImpl
import com.example.musicplayer.domain.repository.AudioRepository
import com.example.musicplayer.domain.usecase.AudioPlayerUseCase
import com.example.musicplayer.domain.usecase.GetOfInSharedPreferencesUseCase
import com.example.musicplayer.domain.usecase.PutInSharedPreferencesUseCase
import com.example.musicplayer.ui.MainActivity.Companion.APP_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

	@Provides
	@Singleton
	fun provideSharedPrefStorage(@ApplicationContext context: Context): SharedPreferences {
		return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
	}

	@Provides
	@Singleton
	fun providePutInSharedPreferences(sharedPreferences: SharedPreferences): PutInSharedPreferencesUseCase {
		return PutInSharedPreferencesUseCase(sharedPreferences)
	}

	@Provides
	@Singleton
	fun provideGetOfInSharedPreferencesUseCase(sharedPreferences: SharedPreferences): GetOfInSharedPreferencesUseCase {
		return GetOfInSharedPreferencesUseCase(sharedPreferences)
	}

	@Provides
	@Singleton
	fun provideMediaMetadataRetriever(): MediaMetadataRetriever {
		return MediaMetadataRetriever()
	}

	@Provides
	@Singleton
	fun provideDataSource(mmr: MediaMetadataRetriever): AudioDataSource {
		return AudioDataSourceImpl(mmr)
	}

	@Provides
	@Singleton
	fun provideDirectoryRepository(dataSource: AudioDataSource): AudioRepository {
		return AudioRepositoryImpl(dataSource)
	}

	@Provides
	@Singleton
	fun provideDirectoryAudioUseCase(@ApplicationContext context: Context, repository: AudioRepository): AudioPlayerUseCase {
		return AudioPlayerUseCase(context, repository)
	}
}