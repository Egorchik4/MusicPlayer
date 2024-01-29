package com.example.musicplayer.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.musicplayer.presentation.state.AudioItemState

class MusicAdapter(
	private val onItemClick: (musicItemState: AudioItemState) -> Unit
) : ListAdapter<AudioItemState, AudioViewHolder>(PaymentsDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		AudioViewHolder.from(parent)

	override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
		holder.bind(getItem(position), onItemClick)
	}
}

class PaymentsDiffCallback : DiffUtil.ItemCallback<AudioItemState>() {

	override fun areItemsTheSame(oldItem: AudioItemState, newItem: AudioItemState): Boolean {
		return if (oldItem is AudioItemState.StopItem && newItem is AudioItemState.StopItem) {
			oldItem.audioEntity.name == newItem.audioEntity.name
		} else if (oldItem is AudioItemState.PlayItem && newItem is AudioItemState.PlayItem) {
			oldItem.audioEntity.name == newItem.audioEntity.name
		} else if (oldItem is AudioItemState.StopItem && newItem is AudioItemState.PlayItem) {
			oldItem.audioEntity.name == newItem.audioEntity.name
		} else if (oldItem is AudioItemState.PlayItem && newItem is AudioItemState.StopItem) {
			oldItem.audioEntity.name == newItem.audioEntity.name
		} else {
			true
		}
	}

	override fun areContentsTheSame(oldItem: AudioItemState, newItem: AudioItemState): Boolean {
		return oldItem == newItem
	}
}