package masayah.mysimple.alarm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import masayah.mysimple.alarm.ActivityAlarmOnLockScreen
import masayah.mysimple.alarm.Constants
import masayah.mysimple.alarm.R

object PreNotification {

    fun notifyPreMessage(context: Context) {

        val notificationChannel = NotificationChannel(
            Constants.CHANNEL_ID_PRE,
            Constants.CHANNEL_DESCRIPTION_PRE,
            NotificationManager.IMPORTANCE_LOW  // 事前通知だから（着信音なし、バイブなし）
        )

        // マネージャー
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(context)
        notificationManagerCompat.createNotificationChannel(notificationChannel)


        val intent = Intent(context, ActivityAlarmOnLockScreen::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            Constants.PRE_NOTIFY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID_PRE)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle("お薬の時間が近づいてます。")
            .setContentText("朝食 後 07:00 のお薬")
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        val notification = builder.build()
        notificationManagerCompat.notify(Constants.PRE_NOTIFY_ID, notification)

    }

    fun cancelPreNotification(context: Context) {
        val ns = AppCompatActivity.NOTIFICATION_SERVICE
        val nMgr = context.getSystemService(ns) as NotificationManager
        nMgr.cancel(Constants.PRE_NOTIFY_ID)
    }

}