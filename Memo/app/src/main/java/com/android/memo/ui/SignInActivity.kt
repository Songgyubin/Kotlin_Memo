package com.android.memo.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.memo.R

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        overridePendingTransition(R.anim.righttoleft, R.anim.none)

    }
    override fun onStart() {
        super.onStart()
        Log.d(localClassName,"onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(localClassName,"onStop")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.nonetoright)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(localClassName,"onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d(localClassName,"onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(localClassName,"onResume")
    }
}