package com.android.memo.util

import android.Manifest
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.memo.R
import com.squareup.picasso.Picasso

fun ImageView.ploadImg(imageUrl: String) {

    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.get().load(R.mipmap.ic_launcher).into(this)
    } else {
        Picasso.get().load(imageUrl)
            .centerCrop()
            .fit()
            .error(R.drawable.noavailable)
            .into(this)
    }
}

fun EditText.getString(): String {
    return this.text.toString()
}

fun TextView.getString(): String {
    return this.text.toString()
}

object Const {
    const val MULTIPLE_PERMISSIONS_CODE = 100

    val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

}