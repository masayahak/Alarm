──────────────────────────────────────────────────────────
課題
──────────────────────────────────────────────────────────
■ 02/24
【済】事前通知のアラーム音やバイブレートを消した
　　・notification channelを事前通知と本通知で分離した
　　・事前通知のNotificationManager.IMPORTANCE_LOW
【済】permissionの最小化
【済】setAlarmClockにしたら、ロック画面が電源OFFの状態のままだったので setAndAllowWhileIdle に戻した。
【済】OS再起動時の自動サービス登録よくわからないから消した
【済】アプリをアンインストールしてから、再インストールしないと権限周りの挙動が正確じゃないかもしれない。
【済】アプリを再インストールした場合は、権限でロック画面上に表示を許可しないといけない


──────────────────────────────────────────────────────────
■ 02/23
【済】音楽は鳴りっぱなしではいけない N分で自動停止する必要がある
【済】アラーム中にアプリを開始したら、アラーム画面を表示。
【済】ユーザーが通知の一覧から通知を消せてしまう。
【済】そうすると、通知が無いから通知画面が表示できない。→飲んだことにもうできない
事前通知のアラームとバイブレートを消したい setSilent(true)すると通知が表示されない


──────────────────────────────────────────────────────────
■ 02/22
【済】電源OFF時のフルスクリーンインテント → 成功
【済】電源ON時は通知 → タップでフルスクリーン → 成功
【済】WakefulBroadcastReceiverを普通のBroadcastReceiverに変更して成功





──────────────────────────────────────────────────────────
■ 02/21
電源は入るようになったがWakefulBroadcastReceiver deprecated
【済】通知をタップしても「暗証番号を～～」fullscreenintent？ → と思ったけど解決しないかも
【済】 → これ現状で良いんじゃね？勝手に暗証番号解除するか？って話よね

アラーム中にアプリを開始したら、アラーム画面を表示。

──────────────────────────────────────────────────────────
■ 02/20
【済】電源OFFで通知がなったら、電源が入らないといけない。
【済】かつ通知が見える状態。

──────────────────────────────────────────────────────────
■ 02/19
【済】・アラーム時刻に音楽を再生したい。
【済】・再生は簡単。実装済み。
【済】・当然再生された音楽を停止したい。
【済】・再生開始はレシバー、停止要求はアクティビティと違う。
【済】・MediaPlayerをシングルトン化しないとだめ
【済】 ↑ これが、サービス化なのかオブジェクト化なのか色々困ってる

──────────────────────────────────────────────────────────
■ 課題 02/18
──────────────────────────────────────────────────────────
・レシーバーでアラームを受け取った後、Activityを起動できない。
　（タスクとしてActivityが残っていれば動くんだけど、無いとダメ）
・レシバー経由じゃなくても、アラームに指定するPendingIntentに直接アクティビティ起動を
　書きたいが、上記がそもそもうまく行ってないので、この実装ができていない。
↑
・バックスタックにないアプリのActivity起動は制限され、通知しろに変わった。




──────────────────────────────────────────────────────────
■ アラームの仕様
──────────────────────────────────────────────────────────
・アラームは2種類ある。
1) 30分前に通知するアラーム。
　　このアラームで音を鳴らすアラームを作るからDOZでも強制起動しないとダメ
　　ただし、画面の電源ONはいらないし、音もいらない
2) 予定時刻にDOZでも強制起動し、音を鳴らすアラーム。

──────────────────────────────────────────────────────────
▼ 30分前通知アラーム
──────────────────────────────────────────────────────────
アラームの作成・再作成タイミング
 → アプリを終了するタイミングで、
──────────────────────────────────────────────────────────
setAndAllowWhileIdleで作成されるアラーム

・予定時刻の30分前に通知が作成される。
　　朝食前のお薬 ＸＸＸＸＸＸＸｘ
　　XX:XXにアラームが鳴ります。
　　　　「お薬を飲み終えた。」
・この通知が作成されるタイミングで正確な時刻の音鳴りアラームのスケジュールが作成される。
・音鳴りアラームはN分後の音消しアラームをセットで作成する。
・このタイミングでアプリアイコンの右上に丸がつく。
・この通知が表示されているタイミングでアイコンをクリックすると
　「朝食前のお薬 ＸＸＸＸＸＸＸＸＸを飲みましたか？」と確認画面が表示される。
・飲み終えるか、予定時刻になると上記通知は自動的に削除される。


──────────────────────────────────────────────────────────
▼ 予定時刻音鳴りアラーム
──────────────────────────────────────────────────────────
・アラームONで、予定時刻に飲み終えてないと通知されアラームが鳴る。
　通知のボタンをクリックするとアプリが起動しボタンでアラームOFF
　「朝食前のお薬 ＸＸＸＸＸＸＸＸＸを飲みましたか？」と確認され
 　　「お薬を飲み終えた。」
    「30分後にもう一度アラームを鳴らす」
    をタップできる。

──────────────────────────────────────────────────────────
▼
──────────────────────────────────────────────────────────

package masayah.mysimple.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
            if (isAfter5Seconds) {
                testAfter5Seconds(this)
            } else {
                testActivateAlarm(this)
            }

            // アラームを作成したらアプリ終了する。
            this.finish()
            this.moveTaskToBack(true)

        }

        // 通知機能の直接起動テスト
        binding.pushNotification.setOnClickListener {
//            testDirectTime(this)
            NotificationReceiver.notifyMessage(this)
        }
    }

    // 正確な時刻にアクティビティを強制機動する
    private fun testActivateAlarm (context : Context) {

        val activityIntent = Intent(context, ActivityAlarm::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        // requestCodeはIntent毎に分ける
        val activityPendingIntent: PendingIntent? =
            PendingIntent.getActivity(
                context,
            764,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        // 既にアラームが登録されていたら、前のアラームをキャンセルする
        if (activityPendingIntent != null && alarmMgr != null) {
            alarmMgr.cancel(activityPendingIntent)
        }

        // Set the alarm to start at approximately 2:00 p.m.
        val calendar: Calendar =
            Calendar.getInstance(
                TimeZone.getTimeZone("Asia/Tokyo"),
                Locale.JAPAN).apply {
            timeInMillis = System.currentTimeMillis()
        }.also {
            it.add(Calendar.MINUTE ,1)
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
            activityPendingIntent
        )
    }


    // テスト用に5秒後にアラームをセットする
    private fun testAfter5Seconds (context : Context) {

        val alarmIntent = Intent(context, NotificationReceiver::class.java)

        // requestCodeはIntent毎に分ける
        val alarmPendingIntent: PendingIntent? =
            PendingIntent.getBroadcast(context, 765, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        // 既にアラームが登録されていたら、前のアラームをキャンセルする
        if (alarmPendingIntent != null && alarmMgr != null) {
            alarmMgr.cancel(alarmPendingIntent)
        }

        // このテストでは予定時刻はキューを要求されてから「5秒後」とする。
        val calendar: Calendar =
            Calendar.getInstance(
                TimeZone.getTimeZone("Asia/Tokyo"),
                Locale.JAPAN).apply {
                timeInMillis = System.currentTimeMillis()
            }.also {
                it.add(Calendar.SECOND ,5)
            }

        val hourString = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val minuteString = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        val secondString = calendar.get(Calendar.SECOND).toString().padStart(2, '0')
        val messageString = "予定時刻 $hourString:$minuteString:$secondString"

        Toast.makeText(this,messageString, Toast.LENGTH_LONG).show()


        // setInexactRepeating 不正確なタイミングでの繰り返し
        // （Dozeモードやスリープでは起動する必要がない
        alarmMgr?.setInexactRepeating(

            // RTC : 予定時刻でアラームをセット（スリープ解除なし）
            AlarmManager.RTC,

            calendar.timeInMillis,

            // 毎日同じ時間に繰り返す
            AlarmManager.INTERVAL_DAY,

            // 実行内容
            alarmPendingIntent
        )
    }

}