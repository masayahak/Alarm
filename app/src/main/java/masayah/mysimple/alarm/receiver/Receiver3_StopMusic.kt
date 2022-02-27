package masayah.mysimple.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import masayah.mysimple.alarm.AlarmMusic

// 予定時刻の通知で音楽をリピート再生する
// 無限に電池を使ってほしくないので、予定時刻＋N分にこのレシーバーを起動する
// 再生中の音楽がある場合は、停止する。

class Receiver3_StopMusic : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        AlarmMusic.dispose()
    }
}