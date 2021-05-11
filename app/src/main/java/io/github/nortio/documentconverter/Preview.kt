package io.github.nortio.documentconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.webkit.WebView
import java.io.File

class Preview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val html = intent.getStringExtra(PREVIEWHTML)
        val outputFile = File(this.cacheDir, "output.html");
        if (html != null) {
            outputFile.writeText(html)
        } else {
            outputFile.writeText("There was a problem creating the temporary page.")
        }
        val myWebView = WebView(this)
        if (html != null) {
            val encoded = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
            myWebView.loadData(encoded, "text/html", "base64")
        }
        setContentView(myWebView)

    }
}