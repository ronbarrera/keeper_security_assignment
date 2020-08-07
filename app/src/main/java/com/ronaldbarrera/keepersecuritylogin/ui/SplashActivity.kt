package com.ronaldbarrera.keepersecuritylogin.ui

import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ronaldbarrera.keepersecuritylogin.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        when (val drawable = image_logo_anim.drawable) {
            is AnimatedVectorDrawable -> {
                drawable.also {
                    it.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            super.onAnimationEnd(drawable)
                            val loginActivityIntent = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(loginActivityIntent)
                            this@SplashActivity.finish()
                        }
                    })
                    it.start()
                }
            }
        }
    }
}

