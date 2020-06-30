package com.android.memo.util

import android.text.TextUtils
import android.widget.ImageView
import com.android.memo.R
import com.squareup.picasso.Picasso

fun ImageView.ploadImg(imageUrl: String) {

    if(TextUtils.isEmpty(imageUrl)){
        Picasso.get().load(R.mipmap.ic_launcher).into(this)
    }
    else{
        Picasso.get().load(imageUrl)
            .centerCrop()
            .fit()
            .error(R.drawable.noavailable)
            .into(this)
    }


}