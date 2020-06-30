package com.android.memo.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.memo.AppDatabase
import com.android.memo.R
import com.android.memo.data.Memo
import com.android.memo.util.getString
import com.android.memo.util.ploadImg
import kotlinx.android.synthetic.main.activity_memo.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.toast
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MemoActivity : AppCompatActivity() {

    lateinit var db: AppDatabase


    lateinit var imageFilePath: String
    lateinit var photoUri: Uri

    var isChecked = false
    var id = -1
    var imgUri = ""
    var type = "new"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        type = intent.getStringExtra("type")

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

    fun onClick(v: View) {
        when (v.id) {
//            R.id.edit_toolbar_save_btn -> saveMemo()
            R.id.camera_btn -> sendTakePhotoIntent()
            R.id.gallery_btn -> getPhoto()
            R.id.edit_toolbar_back_btn -> finish()
        }
    }


    private fun saveMemo(id: Int) {
        this.id = id
        if (edit_content_ed.getString().isEmpty())
            toast("내용을 입력하세요")
        else {
            val date = edit_toolbar_date_tv.getString()


            var title = edit_title_ed.getString()
            if (edit_title_ed.getText().isEmpty())
                title = edit_title_ed.hint.toString()

            val content = edit_content_ed.getString()

            if (type == "new") {
                insertMemo(Memo(0, title, content, imgUri, date, false))
            } else {
                insertMemo(Memo(id, title, content, imgUri, date, isChecked))
            }

        }
    }

    private fun insertMemo(memo: Memo) {

        runBlocking {
            val insert = launch {
                db.memoDao().insertMemo(memo)
            }
            insert.join()
            toast("저장이 완료 되었습니다.")
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA) {
            edit_iv_picture.ploadImg(photoUri.toString())
            imgUri = photoUri.toString()
        } else if (requestCode == GALLERY) {
            photoUri = data!!.data!!
            edit_iv_picture.ploadImg(photoUri.toString())
            imgUri = photoUri.toString()
        }
    }


    // 사진 데이터 가공
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "MEMO_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        imageFilePath = image.absolutePath
        return image
    }

    private fun sendTakePhotoIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            val photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(this, packageName, photoFile)

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

            startActivityForResult(intent, CAMERA)
        }
    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, GALLERY)
    }


    companion object {
        private const val TAG = "MemoActivity.kt"
        private const val CAMERA = 1001
        private const val GALLERY = 1002
    }
}