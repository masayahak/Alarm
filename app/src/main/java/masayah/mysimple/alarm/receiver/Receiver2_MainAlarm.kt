package masayah.mysimple.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import masayah.mysimple.alarm.AlarmMusic
import masayah.mysimple.alarm.notification.PreNotification
import masayah.mysimple.alarm.notification.OnLockScreenNotification

class Receiver2_ActivateAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        // レシーバーが予定時刻に通知を受け取ったときデバイスの状態は大きく分けて２通りある。
        // 1) デバイスは休止状態。
        // 2) デバイスはONの状態。

        val pm = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isInteractive: Boolean = pm.isInteractive

        // 本通知を発行するので事前通知は消す
        PreNotification.cancelPreNotification(context)

        // 通知
        OnLockScreenNotification.notifyOnLockScreenMessage(context, isInteractive)

        // アラームを鳴らす
        // 音とバイブレーションに関しては、Notification に付加する方法を色々トライしたが、
        // 機種依存で制限が色々かかったので自分で制御することとした
        AlarmMusic.create(context)
        AlarmMusic.start()

    }
}