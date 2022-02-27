package masayah.mysimple.alarm.notification

import android.app.Notification
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

object OnLockScreenNotification {

    fun notifyOnLockScreenMessage(context: Context, isInteractive: Boolean) {

        val notificationChannel = NotificationChannel(
            Constants.CHANNEL_ID_ALARM,
            Constants.CHANNEL_DESCRIPTION_ALARM,
            NotificationManager.IMPORTANCE_HIGH
        )

        if (!isInteractive)
            notificationChannel.apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }

        // マネージャー
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(context)
        notificationManagerCompat.createNotificationChannel(notificationChannel)

        val intent = Intent(context, ActivityAlarmOnLockScreen::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            Constants.NOTIFY_ON_LOCK_SCREEN_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID_ALARM)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle("お薬の時間です。")
            .setContentText("朝食 後 07:00 のお薬")
            .setOngoing(true)

        builder.apply {
            if (!isInteractive) {
                setFullScreenIntent(pendingIntent, true)
            } else {
                setContentIntent(pendingIntent)
            }
        }

        val notification = builder.build()
        notificationManagerCompat.notify(Constants.NOTIFY_ON_LOCK_SCREEN_NOTIFY_ID, notification)

    }

    fun cancelNotification(context: Context) {
        val ns = AppCompatActivity.NOTIFICATION_SERVICE
        val nMgr = context.getSystemService(ns) as NotificationManager
        nMgr.cancel(Constants.NOTIFY_ON_LOCK_SCREEN_NOTIFY_ID)
    }

}