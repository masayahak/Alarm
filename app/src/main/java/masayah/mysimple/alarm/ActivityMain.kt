package masayah.mysimple.alarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import masayah.mysimple.alarm.databinding.ActivityMainBinding
import masayah.mysimple.alarm.receiver.Receiver1_PreAlarm
import masayah.mysimple.alarm.receiver.Receiver2_ActivateAlarm
import masayah.mysimple.alarm.receiver.Receiver3_StopMusic
import java.util.*


class ActivityMain : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // アプリ起動時にアラームに関する通知が表示されている場合は
        //アラーム画面を表示する。

        // 通知が存在するか？
        if (isNotificationExist(this)) {
            // アラーム画面を表示する
            val intent = Intent(applicationContext, ActivityAlarmOnLockScreen::class.java)
            startActivity(intent)
        }

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // アラームのテスト
        binding.setPreAlarm.setOnClickListener {

            // 事前通知
            setPreAlarm(this, 5)
            // 本通知予約
            setAlarm(this, 30)
            // N分後に音楽を強制的に停止する
            setAutoStopMusic(this, 60)

            // 事前アラームを作成したらアプリ終了する。
            this.finish()
            this.moveTaskToBack(true)
        }

        // 音楽再生テスト
        binding.soundTest.setOnClickListener {
            soundTest()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            AlarmMusic.dispose()
        }
        catch (ex: Exception) {
        }
    }

    private fun soundTest() {
        var soundTestString = binding.soundTest.text

        if (Regex(" 再生中").containsMatchIn(soundTestString)) {
            // アラームを止める
            try {
                AlarmMusic.stop()
            }
            catch (ex: Exception) {
            }

            soundTestString = "音がちゃんと鳴るかテスト"
            binding.soundTest.text = soundTestString

        } else {
            // アラームを鳴らす
            AlarmMusic.create(this.applicationContext)
            AlarmMusic.start()

            soundTestString = "音がちゃんと鳴るかテスト 再生中"
            binding.soundTest.text = soundTestString
        }
    }

    companion object {

        // アラーム通知を表示中か？
        private fun isNotificationExist(context: Context) : Boolean {
            val mNotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notifications = mNotificationManager.activeNotifications
            for (notification in notifications) {
                if (notification.id == Constants.PRE_NOTIFY_ID) {
                    return true
                }
                if (notification.id == Constants.NOTIFY_ON_LOCK_SCREEN_NOTIFY_ID) {
                    return true
                }
            }
            return false
        }

        // ──────────────────────────────────────────────────────
        // 事前アラーム
        // ──────────────────────────────────────────────────────
        fun setPreAlarm(context: Context, duration: Int) {

            val alarmIntent = Intent(context, Receiver1_PreAlarm::class.java)
            val alarmPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    Constants.PRE_NOTIFY_REQUEST_CODE,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )

            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            // 既にアラームが登録されていたら、前のアラームをキャンセルする
            if (alarmPendingIntent != null && alarmMgr != null) {
                alarmMgr.cancel(alarmPendingIntent)
            }

            // 予定時刻を取得
            val alarmCalendar = getAlarmCalendar(duration)

            // Dozeモードでも強制的に起動させる
            alarmMgr?.setAndAllowWhileIdle(
                AlarmManager.RTC,
                alarmCalendar.timeInMillis,
                alarmPendingIntent
            )
        }

        // ──────────────────────────────────────────────────────
        // 本アラーム
        // もしもスマホの電源が入っている場合 → 通知
        // もしも電源OFFでロックされている場合 → フルスクリーン
        // ──────────────────────────────────────────────────────
        fun setAlarm(context: Context, duration: Int) {

            val alarmIntent = Intent(context, Receiver2_ActivateAlarm::class.java)
            val alarmPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    Constants.NOTIFY_ON_LOCK_SCREEN_REQUEST_CODE,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )

            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            // 既にアラームが登録されていたら、前のアラームをキャンセルする
            if (alarmPendingIntent != null && alarmMgr != null) {
                alarmMgr.cancel(alarmPendingIntent)
            }

            // 予定時刻を取得
            val alarmCalendar = getAlarmCalendar(duration)

            // Dozeモードでも強制的に起動させる
            alarmMgr?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmCalendar.timeInMillis,
                alarmPendingIntent
            )

        }

        // ──────────────────────────────────────────────────────
        // アラーム音楽停止
        // ──────────────────────────────────────────────────────
        fun setAutoStopMusic(context: Context, duration: Int) {

            val stopMusicIntent = Intent(context, Receiver3_StopMusic::class.java)
            val stopMusicPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    Constants.STOP_MUSIC_REQUEST_CODE,
                    stopMusicIntent,
                    PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )

            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            // 既にアラームが登録されていたら、前のアラームをキャンセルする
            if (stopMusicPendingIntent != null && alarmMgr != null) {
                alarmMgr.cancel(stopMusicPendingIntent)
            }

            // 予定時刻を取得
            val alarmCalendar = getAlarmCalendar(duration)

            // Dozeモードでも強制的に起動させる
            alarmMgr?.setAndAllowWhileIdle(
                AlarmManager.RTC,
                alarmCalendar.timeInMillis,
                stopMusicPendingIntent
            )
        }

        // 予定時刻を取得
        private fun getAlarmCalendar (duration: Int): Calendar {

            val calendar: Calendar =
                Calendar.getInstance(
                    TimeZone.getTimeZone("Asia/Tokyo"),
                    Locale.JAPAN
                ).apply {
                    timeInMillis = System.currentTimeMillis()
                }.also {
                    // 現在時刻のN秒後とする
                    it.add(Calendar.SECOND, duration)
                }

            return calendar
        }

    }
}