package com.webview.sandbox

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WebView dibuat programatik (tanpa layout XML)
        webView = WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
        }

        // Registrasi JS Bridge dengan try-catch untuk keamanan
        try {
            webView.addJavascriptInterface(WebAppBridge(this, this), "Android")
        } catch (e: SecurityException) {
            Log.e("MainActivity", "Gagal menambahkan JavascriptInterface: ${e.message}")
        }

        setContentView(webView)
        webView.loadUrl("file:///android_asset/www/index.html")

        // Back handler: navigasi WebView dulu, kalau sudah tidak bisa goBack baru finish.
        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        // Bersihkan WebView agar tidak memory leak
        webView.destroy()
        super.onDestroy()
    }
}