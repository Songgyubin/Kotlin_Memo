package com.android.memo.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.memo.AppDatabase
import com.android.memo.R
import com.android.memo.data.Memo
import com.android.memo.ui.adapter.MemoAdapter
import com.android.memo.util.Const.MULTIPLE_PERMISSIONS_CODE
import com.android.memo.util.Const.requiredPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var notificationManager: NotificationManager
    private lateinit var adapter: MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        db = AppDatabase.getInstance(this)!!

        main_recycler_view.layoutManager = LinearLayoutManager(this)

        floating_btn.setOnClickListener {
            startActivity<MemoActivity>(
                "type" to "new"
            )
        }

        db.memoDao().getAllMemos().observe(this, Observer { memos ->
            adapter = MemoAdapter(memos as ArrayList<Memo>)
            main_recycler_view.adapter = adapter

            adapter.setOnItemClickListener(object : MemoAdapter.OnItemClickListener {
                override fun onItemClick(v: View, position: Int) {
                    startActivity<MemoActivity>(
                        "memo" to memos[position],
                        "type" to "edit"
                    )
                }

                override fun onItemLongClick(v: View, position: Int) {
                    alert("알림", "삭제를 원하세요?") {
                        yesButton {
                            deleteMemo(memos[position].id)
                        }
                        noButton {
                            it.dismiss()
                        }
                    }.show()
                }

                override fun onCheckClick(v: View, position: Int) {
                    UpdateMemo(memos[position])

                }

            })

        })

    }


    private fun deleteMemo(id: Int) {

        runBlocking {
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                val delete = launch {
                    db.memoDao().deleteMemoById(id)
                }
                delete.join()
                CoroutineScope(Dispatchers.Main).launch { toast("삭제가 완료 되었습니다.") }

            }


        }
    }

    private fun UpdateMemo(memo: Memo) {
        val memo = memo
        memo.isChecked = !memo.isChecked
        Log.d(TAG, "전: ${memo.isChecked}")
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val update = launch { db.memoDao().insertMemo(memo) }
            update.join()
            CoroutineScope(Dispatchers.Main).launch {
                if (memo.isChecked)
                    showCustomLayoutNotification(memo)
                else notificationManager.cancelAll()
                
                Log.d(TAG, "후: ${memo.isChecked}")

            }

        }


    }

    private fun showCustomLayoutNotification(memo: Memo) {

        val contentView = RemoteViews(packageName, R.layout.activity_memo_notify)

        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher)
        contentView.setTextViewText(R.id.title, "Custom notification")
        contentView.setTextViewText(R.id.text, "This is a custom layout")

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.checked_star)
            .setContentTitle(memo.title)
            .setContentText(memo.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL

        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "mychannel"
            val description = "mymemo"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    // 권한 체크
    private fun checkPermissions() {

        var rejectedPermissionList = ArrayList<String>()

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                rejectedPermissionList.add(permission)
            }
        }
        if (rejectedPermissionList.isNotEmpty()) {

            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(
                this,
                rejectedPermissionList.toArray(array),
                MULTIPLE_PERMISSIONS_CODE
            )
        }
    }

    companion object {
        private const val TAG = "MainActivity.kt"
        private const val CHANNEL_ID = "checkedmemo"
    }

}

