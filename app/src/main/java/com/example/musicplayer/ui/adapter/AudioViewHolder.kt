package com.example.musicplayer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.databinding.AudioItemBinding
import com.example.musicplayer.presentation.state.AudioItemState

class AudioViewHolder(
	private val context: Context,
	private val binding: AudioItemBinding
) : RecyclerView.ViewHolder(binding.root) {

	companion object {

		fun from(parent: ViewGroup): AudioViewHolder {
			val inflater = LayoutInflater.from(parent.context)
			val binding = AudioItemBinding.inflate(inflater, parent, false)
			return AudioViewHolder(binding.root.context, binding)
		}
	}

	fun bind(
		audioItemState: AudioItemState,
		onItemClick: (musicItemState: AudioItemState) -> Unit
	) {
		with(binding) {
			when (audioItemState) {
				is AudioItemState.PlayItem -> {
					textNameAudio.text = audioItemState.audioEntity.name
					textTimeActual.text = audioItemState.audioEntity.actualDuration
					textTime.text = audioItemState.audioEntity.timeOfDuration
					progressBar.max = audioItemState.audioEntity.maxOfDurationInt
					progressBar.progress = audioItemState.audioEntity.actualDurationInt
					progressBar.visibility = View.VISIBLE
					root.setCardBackgroundColor(context.getColor(R.color.greyCardView))
				}

				is AudioItemState.StopItem -> {
					textNameAudio.text = audioItemState.audioEntity.name
					textTimeActual.text = audioItemState.audioEntity.actualDuration
					textTime.text = audioItemState.audioEntity.timeOfDuration
					progressBar.max = audioItemState.audioEntity.maxOfDurationInt
					progressBar.progress = 0
					progressBar.visibility = View.GONE
					root.setCardBackgroundColor(context.getColor(R.color.white))
				}
			}
			root.setOnClickListener { onItemClick(audioItemState) }
		}
	}
}