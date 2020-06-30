package com.android.memo.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.memo.AppDatabase
import com.android.memo.R
import com.android.memo.data.Memo
import com.android.memo.util.ploadImg
import kotlinx.android.synthetic.main.activity_memo.*
import java.time.LocalDate

class MemoActivity : AppCompatActivity() {

    lateinit var db: AppDatabase


    lateinit var imageFilePath: String
    lateinit var photoUri: Uri

    var isChecked = false
    var id = 0
    var imgUri = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val type = intent.getStringExtra("type")

        db = AppDatabase.getInstance(this)!!

        var date = LocalDate.now().toString().replace('-', '.')
        edit_toolbar_date_tv.text = date


        if (type == "edit") {
            var memo = intent.getSerializableExtra("memo") as Memo
            edit_title_ed.setText(memo.title)
            edit_content_ed.setText(memo.content)
            id = memo.id
            isChecked = memo.isChecked
            edit_iv_picture.ploadImg(memo.image)
            imgUri = memo.image
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA){
            edit_iv_picture.ploadImg(photoUri.toString())
            imgUri = photoUri.toString()
        }
        else if (requestCode == GALLERY){
            photoUri = data!!.data!!
            edit_iv_picture.ploadImg(photoUri.toString())
            imgUri = photoUri.toString()
        }
    }



    companion object {
        private const val TAG = "MemoActivity.kt"
        private const val CAMERA = 1001
        private const val GALLERY = 1002
    }
}