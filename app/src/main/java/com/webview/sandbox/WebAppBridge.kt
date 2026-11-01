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
        // Mengambil versi langsung dari PackageManager (tanpa butuh BuildConfig)
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0"
        } catch (e: Exception) {
            "1.0"
        }
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