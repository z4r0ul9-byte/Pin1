package com.webview.sandbox

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val navigateToMain = Runnable {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay singkat 1200ms lalu pindah ke MainActivity.
        // Pakai Handler untuk menghindari dependency lifecycle-runtime-ktx.
        handler.postDelayed(navigateToMain, 1200L)
    }

    override fun onDestroy() {
        // Hindari callback menembak setelah activity dihancurkan.
        handler.removeCallbacks(navigateToMain)
        super.onDestroy()
    }
}