package masayah.mysimple.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import masayah.mysimple.alarm.notification.PreNotification


// ──────────────────────────────────────────────────────────
// 事前通知
// ──────────────────────────────────────────────────────────
class Receiver1_PreAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // 通知
        PreNotification.notifyPreMessage(context!!)
    }
}