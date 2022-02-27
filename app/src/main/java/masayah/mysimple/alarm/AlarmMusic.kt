package masayah.mysimple.alarm

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.lang.Exception

object AlarmMusic {

    lateinit private var _mediaPlayer: MediaPlayer

    fun create(context: Context) {

        try {
            dispose()
        }
        catch (ex: Exception) { }

        // MediaPlayer 作成
        _mediaPlayer = MediaPlayer.create(context, R.raw.pop_culture)
        _mediaPlayer.isLooping=true

    }

    fun start() {
        try {
            _mediaPlayer.start()
        }
        catch (ex: Exception) { }
    }

    fun stop() {
        try {
            _mediaPlayer.stop()
        }
        catch (ex: Exception) { }
    }

    fun dispose() {
        try {
            _mediaPlayer.stop()
            _mediaPlayer.release()
        }
        catch (ex: Exception) { }
    }

}