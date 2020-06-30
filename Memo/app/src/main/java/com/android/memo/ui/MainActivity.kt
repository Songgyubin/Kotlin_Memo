package com.android.memo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.memo.AppDatabase
import com.android.memo.R

class MainActivity : AppCompatActivity() {

    lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    companion object{
    private const val TAG = "MainActivity.kt"
    }

}