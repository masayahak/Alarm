package masayah.mysimple.alarm

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import masayah.mysimple.alarm.databinding.ActivityMainBinding
import java.util.*


class ActivityMain : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // アラーム（時間指定：５秒ご起動）のテスト
        binding.setAlarm.setOnClickListener {

            val isAfter5Seconds = binding.after5Seconds.isChecked

            // アラーム（特定の時間に起動するキュー）を作成する。
            setAlarm(this, isAfter5Seconds)

            // アラームを作成したらアプリ終了する。
            this.finish()
            this.moveTaskToBack(true)

        }

        // 通知機能の直接起動テスト
        binding.pushNotification.setOnClickListener {
            NotificationReceiver.notifyMessage(this)
        }
    }

    // ──────────────────────────────────────────────────────
    // 指定された時刻（テストでは5秒後）に、自分が登録したサービスを起動しろと登録する。
    // 起動して欲しいサービス：NotificationReceiver
    // ──────────────────────────────────────────────────────
    private fun setAlarm(context: Context, isAfter5Seconds: Boolean) {

        val alarmIntent = Intent(context, NotificationReceiver::class.java)

        // requestCodeが0だと、ハマるケースがあったらしい
        val alarmPendingIntent: PendingIntent? =
            PendingIntent.getBroadcast(context, 764, alarmIntent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        // 既にアラームが登録されていたら、前のアラームをキャンセルする
        if (alarmPendingIntent != null && alarmMgr != null) {
            alarmMgr.cancel(alarmPendingIntent)
        }

        if (isAfter5Seconds) {
            testAfter5Seconds(alarmMgr, alarmPendingIntent)
        } else {
            testDirectTime(alarmMgr, alarmPendingIntent)
        }
    }

    // 5分後の時刻を予定時刻としてアラームをセットする
    private fun testDirectTime (alarmMgr : AlarmManager?, alarmPendingIntent: PendingIntent?) {

        // Set the alarm to start at approximately 2:00 p.m.
        val calendar: Calendar =
            Calendar.getInstance(
                TimeZone.getTimeZone("Asia/Tokyo"),
                Locale.JAPAN).apply {
            timeInMillis = System.currentTimeMillis()
        }.also {
            it.add(Calendar.MINUTE ,5)
        }

        val hourString = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val minuteString = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        val messageString = "予定時刻 $hourString:$minuteString"

        Toast.makeText(this,messageString, Toast.LENGTH_LONG).show()


        // Dozeモードでも強制的に起動させる setAndAllowWhileIdle
        alarmMgr?.setAndAllowWhileIdle(

            // RTC : 時刻（AM08:00など）でアラームをセット
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )
    }


    // テスト用に5秒後にアラームをセットする
    private fun testAfter5Seconds (alarmMgr : AlarmManager?, alarmPendingIntent: PendingIntent?) {

        // Dozeモードでも強制的に起動させる setAndAllowWhileIdle
        alarmMgr?.setAndAllowWhileIdle(

            // ELAPSED_REALTIME_WAKEUP : 経過時間（30秒後など）でアラームをセット
            AlarmManager.ELAPSED_REALTIME_WAKEUP,

            // このテストでは予定時刻はキューを要求されてから「5秒後」とする。
            // 実装は当然AM8:00だったりの直接時刻指定
            SystemClock.elapsedRealtime() + 5 * 1000,
            alarmPendingIntent
        )
    }

}