package masayah.mysimple.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import masayah.mysimple.alarm.databinding.ActivityAlarmOnLockScreenBinding
import masayah.mysimple.alarm.notification.OnLockScreenNotification
import java.lang.Exception

class ActivityAlarmOnLockScreen : AppCompatActivity() {

    private var _binding: ActivityAlarmOnLockScreenBinding? = null
    private val binding get() = _binding!!

    private fun showOnLockScreenAndTurnScreenOn() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showOnLockScreenAndTurnScreenOn()

        _binding = ActivityAlarmOnLockScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 飲んだボタン長押し
        binding.buttonTaken.setOnLongClickListener {

            // アラーム音を止める
            try {
                AlarmMusic.dispose()
            } catch(ex: Exception) {}

            // 通知を消す
            OnLockScreenNotification.cancelNotification(this)

            // アプリ終了
            this.finish()
            this.moveTaskToBack(true)

            true
        }

    }

}
