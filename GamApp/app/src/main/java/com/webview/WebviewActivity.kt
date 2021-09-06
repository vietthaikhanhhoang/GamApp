package com.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.barservicegam.app.R

class WebviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webview = findViewById<WebView>(R.id.webviewPage)
        webview.loadUrl("http://192.168.0.106:2000/policy")

    }
}