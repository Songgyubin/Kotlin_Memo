package com.android.memo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.memo.R
import com.android.memo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var isMember = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
    }

    fun startSignUpActivity(){
        this.isMember = false
        startActivity(Intent(this,SignUpActivity::class.java))
        overridePendingTransition(R.anim.none, R.anim.nonetoright )

    }
    fun startSignInActivity(){
        this.isMember = true
        startActivity(Intent(this,SignInActivity::class.java))
        overridePendingTransition(R.anim.none, R.anim.nonetoleft )

    }

    override fun onStart() {
        super.onStart()
        Log.d(localClassName,"onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(localClassName,"onStop")
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