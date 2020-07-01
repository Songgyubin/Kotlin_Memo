package com.android.memo.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.memo.R
import com.android.memo.data.Memo
import com.android.memo.ui.MainActivity

class NotificationService : Service() {


    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "mychannel"
            val description = "mymemo"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val contentView = RemoteViews(packageName, R.layout.activity_memo_notify)
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher)
        contentView.setTextViewText(R.id.title, "Custom notification")
        contentView.setTextViewText(R.id.text, "This is a custom layout")

        val memo = intent!!.getSerializableExtra("memo") as Memo

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.checked_star)
            .setContentTitle(memo.title)
            .setContentText(memo.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build()

        notification.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(1, notification)

        startForeground(1,notification)

        if (intent!!.getStringExtra("isCancel") == "yes")
            {
                // 수정 필요
            notificationManager.cancelAll()
}
        /*val intent =  Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT)
        pendingIntent.send()*/

        return START_STICKY
    }

    companion object {
        private const val TAG = "NotificationService.kt"
        private const val CHANNEL_ID = "checkedmemo"
    }
}
