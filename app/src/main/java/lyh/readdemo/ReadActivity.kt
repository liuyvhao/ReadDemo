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

    override fun onDestroy() {
        super.onDestroy()
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
