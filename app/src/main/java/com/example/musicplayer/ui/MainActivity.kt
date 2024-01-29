package com.example.musicplayer.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.presentation.MainViewModel
import com.example.musicplayer.presentation.state.AudioState
import com.example.musicplayer.ui.adapter.MusicAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	companion object {

		private const val TYPE_OF_FILE = "*/*"
		const val REQUEST_CODE = 0
		const val APP_PREFERENCES = "APP_PREFERENCES"
		const val KEY_SHARED_PREFERENCES = "KEY_SHARED_PREFERENCES"
		const val NO_PERMISSION = "NO_PERMISSION"
	}

	private lateinit var binding: ActivityMainBinding
	private val viewModel: MainViewModel by viewModels()
	private lateinit var adapter: MusicAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		bindAdapter()
		setObserves()
		setListener()
	}

	private fun bindAdapter() {
		adapter = MusicAdapter(viewModel::clickToItem)
		binding.recyclerView.adapter = adapter
	}

	private fun setObserves() {
		viewModel.musicLive.observe(this) {
			when (it) {
				is AudioState.Initial -> {
					handleInitial()
				}

				is AudioState.Content -> {
					handleContent(it)
				}
			}
		}
	}

	private fun handleInitial() {
		with(binding) {
			btnFolder.visibility = View.VISIBLE
			playerInterface.visibility = View.GONE
			recyclerView.visibility = View.GONE
		}
	}

	private fun handleContent(audioState: AudioState.Content) {
		with(binding) {
			btnFolder.visibility = View.GONE
			playerInterface.visibility = View.VISIBLE
			recyclerView.visibility = View.VISIBLE
			btnPlayPause.isChecked = audioState.enableButton
		}
		adapter.submitList(audioState.list)
	}

	private fun setListener() {
		with(binding) {
			btnNext.setOnClickListener {
				viewModel.nextAudio()
			}
			btnPrevious.setOnClickListener {
				viewModel.previousAudio()
			}
			btnPlayPause.setOnClickListener {
				if (btnPlayPause.isChecked) {
					viewModel.playAudio()
				} else {
					viewModel.stopAudio()
				}
			}
			btnFolder.setOnClickListener {
				if (checkPermission()) {
					getMusicFileContract.launch(TYPE_OF_FILE)
				} else {
					showMessage(NO_PERMISSION)
				}
			}
		}
	}

	private fun checkPermission(): Boolean {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			return if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO) !=
				PackageManager.PERMISSION_GRANTED
			) {
				ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), REQUEST_CODE)
				false
			} else {
				true
			}
		} else {
			return if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
				PackageManager.PERMISSION_GRANTED
			) {
				ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
				false
			} else {
				true
			}
		}
	}

	private val getMusicFileContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
		try {
			if (it != null) {
				viewModel.saveMusicFolder(it.toString())
			}
		} catch (e: Exception) {
			showMessage(e.toString())
		}
	}

	private fun showMessage(message: String) {
		Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
	}
}