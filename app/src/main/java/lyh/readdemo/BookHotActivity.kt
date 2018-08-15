package lyh.readdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_book_hot.*
import lyh.util.Head

/**
 * 热门
 */
class BookHotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_hot)
        back.setOnClickListener {
            webView.goBack()
        }
        var websetting = webView.settings
        websetting.javaScriptEnabled = true
        websetting.allowFileAccess = true
        websetting.builtInZoomControls = true
        webView.loadUrl(Head.urlHead)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }
}
