package com.webview.sandbox

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    // Launcher untuk menangani hasil pemilihan file (Upload)
    private val fileChooserLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            filePathCallback?.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(result.resultCode, result.data)
            )
        } else {
            filePathCallback?.onReceiveValue(null)
        }
        filePathCallback = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()

            // ---------------------------------------------------------
            // UPLOAD HANDLER (WebChromeClient)
            // ---------------------------------------------------------
            webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    // Simpan callback agar bisa dipanggil saat user selesai memilih file
                    this@MainActivity.filePathCallback = filePathCallback

                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "*/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }

                    return try {
                        fileChooserLauncher.launch(Intent.createChooser(intent, "Pilih File"))
                        true
                    } catch (e: Exception) {
                        this@MainActivity.filePathCallback = null
                        false
                    }
                }
            }

            // ---------------------------------------------------------
            // DOWNLOAD HANDLER (DownloadListener)
            // ---------------------------------------------------------
            setDownloadListener { url, userAgent, contentDisposition, mimetype, _ ->
                if (url.startsWith("blob:") || url.startsWith("data:")) {
                    Toast.makeText(
                        this@MainActivity,
                        "Download tipe ini belum didukung",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setDownloadListener
                }

                try {
                    val request = DownloadManager.Request(Uri.parse(url))
                    val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
                    
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setMimeType(mimetype)

                    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)

                    Toast.makeText(this@MainActivity, "Mengunduh: $filename", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("MainActivity", "Download gagal: ${e.message}")
                    Toast.makeText(this@MainActivity, "Download gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Registrasi JS Bridge
        try {
            webView.addJavascriptInterface(WebAppBridge(this, this), "Android")
        } catch (e: SecurityException) {
            Log.e("MainActivity", "Gagal menambahkan JavascriptInterface: ${e.message}")
        }

        setContentView(webView)
        webView.loadUrl("file:///android_asset/www/index.html")

        // Back handler
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
        webView.destroy()
        super.onDestroy()
    }
}