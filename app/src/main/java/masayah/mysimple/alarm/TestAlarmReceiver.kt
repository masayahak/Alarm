package masayah.mysimple.alarm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.Context.NOTIFICATION_SERVICE
import android.widget.Toast
import androidx.core.app.NotificationCompat

// ──────────────────────────────────────────────────────
// 指定された時刻（テストでは5秒後）に、 起動して欲しいサービス
// ──────────────────────────────────────────────────────
class TestAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"アラームレシーブ", Toast.LENGTH_LONG).show()
        startActivity(context)
   }

    @SuppressLint("LaunchActivityFromNotification")
    private fun onButtonClick(context: Context?) {

        // notificationManager
        val notificationManager =
            context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 通知に表示するボタンをクリックした時に呼び出して欲しいレシーバーを登録
        // 通知のボタンを押したら TestNotifyReceiver を呼び出す
        val buttonIntent = Intent(context, TestNotifyReceiver::class.java)
        buttonIntent.putExtra("action","TOAST");

        val buttonPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, buttonIntent, 0)

        val notification: Notification
        = NotificationCompat.Builder(context, "test_notification_id_onButtonClick")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("通知タイトル")
            .setContentText("通知メッセージ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(buttonPendingIntent)
            // ボタンを表示
            .addAction(android.R.drawable.ic_dialog_alert, "ボタンの表題",
                buttonPendingIntent)
            .build()

        notificationManager.notify(R.string.app_name, notification)
    }

    // アクティビティを起動
    private fun startActivity(context: Context?) {

        // notificationManager
        val notificationManager =
            context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // 通知のクリックでアクティビティを起動する
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, 0)

        val id = "test_notification_id_startActivity"
        val title = "通知タイトル"
        val message = "通知メッセージ"

        val notification: Notification
        = NotificationCompat.Builder(context, id)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(R.string.app_name, notification)
    }
}