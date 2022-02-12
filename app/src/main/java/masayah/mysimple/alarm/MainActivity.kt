package masayah.mysimple.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import masayah.mysimple.alarm.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.setAlarm.setOnClickListener {

            // アラーム（特定の時間に起動するキュー）を作成する。
            setAlarm()

            // アラームを作成したらアプリを完全に終了する。
            this.finish()
            this.moveTaskToBack(true)
        }
    }

    // ──────────────────────────────────────────────────────
    // 指定された時刻（テストでは5秒後）に、自分が登録したサービスを起動しろと登録する。
    // 起動して欲しいサービス：TestAlarmReceiver
    // ──────────────────────────────────────────────────────
    private fun setAlarm() {

        // 指定してるレシーバーは時間起動に対応した TestAlarmReceiver
        val alarmIntent = Intent(this, TestAlarmReceiver::class.java)
        val alarmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

        val alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            // このテストでは予定時刻はキューを要求されてから「5秒後」とする。
            // 実装は当然AM8:00だったりの直接時刻指定
            SystemClock.elapsedRealtime() + 5 * 1000,
            alarmPendingIntent
        )

    }
}