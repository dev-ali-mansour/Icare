package eg.edu.cu.csds.icare.core.ui.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.core.content.ContextCompat
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class MediaHelper(
    private val context: Context,
) {
    private var mediaPlayer: MediaPlayer? = null
    private var playbackAttributes =
        AudioAttributes
            .Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

    fun play(trackRes: Int) {
        runCatching {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, trackRes)
            mediaPlayer?.let { player ->
                if (player.isPlaying) return
                player.setOnCompletionListener {
                    dispose()
                }
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                        val audioManager =
                            ContextCompat.getSystemService(context, AudioManager::class.java)

                        val focusRequest =
                            AudioFocusRequest
                                .Builder(AudioManager.AUDIOFOCUS_GAIN)
                                .setAudioAttributes(playbackAttributes)
                                .setAcceptsDelayedFocusGain(true)
                                .setOnAudioFocusChangeListener { focusChange ->
                                    runCatching {
                                        when (focusChange) {
                                            AudioManager.AUDIOFOCUS_GAIN -> player.start()
                                            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->
                                                if (player.isPlaying) player.pause()

                                            AudioManager.AUDIOFOCUS_LOSS -> player.release()
                                        }
                                    }.onFailure {
                                        Timber.e("MediaPlayer Error: ${it.stackTrace}")
                                    }
                                }.build()

                        when (audioManager?.requestAudioFocus(focusRequest)) {
                            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> player.start()
                            else -> Timber.d("Audio Focus Not Granted!")
                        }
                    }

                    else -> player.start()
                }
            }
        }.onFailure { Timber.e("MediaHelper Play Error: ${it.stackTrace}") }
    }

    fun dispose() {
        runCatching {
            mediaPlayer?.release()
            mediaPlayer = null
        }.onFailure { Timber.e("MediaHelper Dispose Error: ${it.stackTrace}") }
    }
}
