package lyh.readdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import kotlinx.android.synthetic.main.activity_read.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.view.Gravity
import android.view.WindowManager
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.read_title.view.*
import java.net.URL
import lyh.util.ReadScrollview
import lyh.util.database
import org.jetbrains.anko.db.*


class ReadActivity : AppCompatActivity() {
    private lateinit var imageGetter: ImageGetter
    private var textBody: StringBuffer = StringBuffer()
    var nextChapter: String? = null
    var oldChapter = ""
    var directory = ""
    var thisLink = ""
    private lateinit var popTitle: PopupWindow
    private lateinit var popMenu: PopupWindow
    private var isShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        initView()
        initData(thisLink)
    }

    fun initView() {
        thisLink = intent.getStringExtra("link")
        scrollView.setOnScrollViewToBottomLiatener(object : ReadScrollview.OnScrollViewToBottomLiatener {
            override fun onScrollViewToBottomListener() {
                if ((nextChapter != oldChapter) && (nextChapter != directory)) {
                    initData(nextChapter!!)
                    thisLink = nextChapter!!
                    oldChapter = nextChapter!!
                }
            }

        })

        var popTitleView = layoutInflater.inflate(R.layout.read_title, null, false)
        var popMenuView = layoutInflater.inflate(R.layout.read_menu, null, false)
        popTitle = PopupWindow(popTitleView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false)
        popTitle.animationStyle = R.style.popTitleAnim
        popMenu = PopupWindow(popMenuView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false)
        popMenu.animationStyle = R.style.popMenuAnim
        popTitleView.back.setOnClickListener { finish() }
        text_tv.setOnClickListener {
            if (isShow) {
                popTitle.dismiss()
                popMenu.dismiss()
                full(true)
            } else {
                popTitle.showAtLocation(it, Gravity.TOP, 0, 0)
                popTitle.update()

                popMenu.showAtLocation(it, Gravity.BOTTOM, 0, 0)
                popMenu.update()
                full(false)
            }
            isShow = !isShow
        }

        imageGetter = ImageGetter { source ->
            var drawable: Drawable? = null
            val url: URL
            try {
                url = URL(source)
                drawable = Drawable.createFromStream(url.openStream(), "")  //获取网路图片
            } catch (e: Exception) {
                return@ImageGetter null
            }

            drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable
                    .intrinsicHeight)
            drawable
        }

    }

    private fun full(enable: Boolean) {
        if (enable) {
            val lp = window.attributes
            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = lp
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            val attr = window.attributes
            attr.flags = attr.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN
            window.attributes = attr
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun initData(link: String) {
        doAsync {
            var doc = Jsoup.connect(link).get()
            uiThread {
                loadingLayout.showContent()
                var body = doc.body()
                var wrap = body.getElementsByClass("wrap")
                var cont = wrap[0].getElementsByClass("d_cont")
                var title = cont[0].getElementsByClass("nr_title")[0].select("h3")
                var html = cont[0].getElementsByClass("nr_con")
                textBody.append(title.toString())
                textBody.append(html.toString())
                directory = doc.getElementsByClass("page")[0].select("a")[1].attr("abs:href")
                nextChapter = doc.getElementsByClass("page")[0].select("a").last().attr("abs:href")
                name.text = doc.getElementsByClass("book_title").text()
                text_tv.text = Html.fromHtml(textBody.toString(), imageGetter, null)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        database.use {
            var bName = intent.getStringExtra("name")
            select("collectBook").whereSimple("name=?", bName).exec {
                if (count == 0)
                    insert("collectBook",
                            "name" to bName,
                            "img" to intent.getStringExtra("img"),
                            "link" to thisLink,
                            "time" to System.currentTimeMillis().toString())
                else
                    update("collectBook", "link" to thisLink, "time" to System.currentTimeMillis().toString()).whereSimple("name=?", bName).exec()
            }
        }
    }
}
