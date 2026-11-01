package com.webview.sandbox

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // WebView dibuat secara programatik (tanpa layout XML) agar ringkas.
        webView = WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            // Wajib: agar URL tetap dibuka di dalam WebView, bukan di browser eksternal.
            webViewClient = WebViewClient()
        }

        setContentView(webView)

        webView.loadUrl("file:///android_asset/www/index.html")
    }

    override fun onDestroy() {
        // Bersihkan WebView supaya tidak leak saat activity dihancurkan.
        webView.destroy()
        super.onDestroy()
    }
}