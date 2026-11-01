package com.webview.sandbox

import android.app.Activity
import android.content.Context
import android.os.Build
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppBridge(private val context: Context, private val activity: Activity) {

    @JavascriptInterface
    fun showToast(message: String) {
        // JS Bridge dieksekusi di background thread, jadi UI update wajib via UI thread
        activity.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    @JavascriptInterface
    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    @JavascriptInterface
    fun getDeviceInfo(): String {
        return """{"os":"Android","sdk":${Build.VERSION.SDK_INT},"model":"${Build.MODEL}"}"""
    }

    @JavascriptInterface
    fun exitApp() {
        activity.runOnUiThread {
            activity.finish()
        }
    }
}