package masayah.mysimple.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// ──────────────────────────────────────────────────────
// 通知を発行するレシーバー
//      指定された時刻に、 起動して欲しいサービス。通知を発行する
// ──────────────────────────────────────────────────────
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"アラームレシーブ", Toast.LENGTH_LONG).show()
        notifyMessage(context!!)
   }

    companion object {

        fun notifyMessage(context: Context){

            val intent = Intent(context, ActivityAfterNotification::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                765,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // チャンネル
            val channelId = "i.apps.notifications"
            val description = "Test notification"
            val notificationChannel = NotificationChannel(
                channelId,
                description,
                NotificationManager.IMPORTANCE_HIGH
            )

            // マネージャー
            val mNotificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
            mNotificationManagerCompat.createNotificationChannel(notificationChannel)

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("通知タイトル")
                .setContentText("通知メッセージ")
                .addAction(android.R.drawable.ic_dialog_alert, "ボタンの表題", pendingIntent)

            mNotificationManagerCompat.notify(1234, builder.build())

        }
   }
}