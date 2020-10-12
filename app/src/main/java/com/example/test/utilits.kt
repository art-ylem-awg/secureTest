package com.example.test

import android.os.CountDownTimer
import com.example.test.view.MainActivity

lateinit var APP_ACTIVITY: MainActivity

fun timer(finish:() -> Unit){
    val timer = object : CountDownTimer(400, 100) {
        override fun onTick(p0: Long) {}
        override fun onFinish() {
            finish()
        }
    }
    timer.start()
}