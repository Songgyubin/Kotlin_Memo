package com.android.memo.ui

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.memo.AppDatabase
import com.android.memo.R
import com.android.memo.util.Const.MULTIPLE_PERMISSIONS_CODE
import com.android.memo.util.Const.requiredPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var notificationManager: NotificationManager


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

        db.memoDao().getAllMemos().observe(this, Observer {

        })

    }

    private fun createNotificationChannel() {
        TODO("Not yet implemented")
    }


    private fun deleteMemo(id: Int) {

        runBlocking {

            /*var deffered = async {
                delay(1000L)
                println("world")  // #1
                50 // 서버로 부터 받아온 데이터를 리턴해주는 부분 return은 적지 않는다.
            }
            var dataFromServer = deffered.await()
            println(`Hello $dataFromServer`)*/
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