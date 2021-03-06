package com.android.memo.ui

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.memo.AppDatabase
import com.android.memo.R
import com.android.memo.data.Memo
import com.android.memo.service.NotificationService
import com.android.memo.ui.adapter.MemoAdapter
import com.android.memo.util.Const.MULTIPLE_PERMISSIONS_CODE
import com.android.memo.util.Const.requiredPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_memo_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.*

class MemoListActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var notificationManager: NotificationManager
    private lateinit var adapter: MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_list)
        checkPermissions()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


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

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val update = launch { db.memoDao().insertMemo(memo) }
            update.join()
            CoroutineScope(Dispatchers.Main).launch {
                if (memo.isChecked) {
                    startService<NotificationService>(
                        "memo" to memo,
                        "isCancel" to "no"
                    )
                } else {
                    stopService<NotificationService>()
                }

            }
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

