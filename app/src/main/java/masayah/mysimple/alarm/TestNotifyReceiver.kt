package masayah.mysimple.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

// ──────────────────────────────────────────────────────
// 通知のボタンを押した時に実行してほしいこと
// ──────────────────────────────────────────────────────

class TestNotifyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent!!.getStringExtra("action")
        if (action == "TOAST") {
            Toast.makeText(context,"通知でボタンを押した", Toast.LENGTH_LONG).show()
            return
        }

    }


}