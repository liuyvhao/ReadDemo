package lyh.readdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import kotlinx.android.synthetic.main.activity_read.*
import org.jsoup.Jsoup
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.view.Gravity
import android.view.WindowManager
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.read_menu.view.*
import kotlinx.android.synthetic.main.read_title.view.*
import lyh.util.*
import java.net.URL
import org.jetbrains.anko.db.*
import org.jetbrains.anko.*

/**
 * 阅读页面
 */
class ReadActivity : AppCompatActivity() {
    companion object {
        var instance: ReadActivity? = null
    }

    private lateinit var imageGetter: ImageGetter
    private var textBody: StringBuffer = StringBuffer()
    private var upChapter: String? = null
    var nextChapter: String? = null
    var oldChapter = ""
    var directory = ""
    var thisLink = ""
    private lateinit var popTitle: PopupWindow
    private lateinit var popMenu: PopupWindow
    private var isShow = false
    private var variable by Preference("colorBg", "")
    private lateinit var bName: String
    private var chapters = ArrayList<Chapter>()
    private var isFirst = true
    private var bColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        instance = this
        initView()
        initData(thisLink)
    }

    fun initView() {
        full(true)
        thisLink = intent.getStringExtra("link")
        scrollView.setOnScrollViewToBottomLiatener(object : ReadScrollview.OnScrollViewToBottomLiatener {
            override fun onScrollViewToBottomListener() {
                if ((nextChapter != oldChapter) && (nextChapter != directory)) {
                    initData(nextChapter!!)
                    thisLink = nextChapter!!
                    oldChapter = nextChapter!!
                } else if (nextChapter == directory)
                    toast("已经是最后一章了！！！")
            }

        })

        var popTitleView = layoutInflater.inflate(R.layout.read_title, null, false)
        var popMenuView = layoutInflater.inflate(R.layout.read_menu, null, false)
        popTitle = PopupWindow(popTitleView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false)
        popTitle.animationStyle = R.style.popTitleAnim
        popMenu = PopupWindow(popMenuView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false)
        popMenu.animationStyle = R.style.popMenuAnim
        popTitleView.back.setOnClickListener { finish() }
        popMenuView.directory.setOnClickListener {
            popTitle.dismiss()
            popMenu.dismiss()
            full(true)
            DirectoryDialog(this, bName, chapters, bColor!!, name.text.toString()).show()
            isShow = !isShow
        }
        popMenuView.up_chapter.setOnClickListener {
            if (upChapter != directory) {
                loadingLayout.showLoading()
                textBody.setLength(0)
                thisLink = upChapter!!
                oldChapter = ""
                scrollView.scrollTo(0, 0)
                initData(upChapter!!)
            } else
                toast("已经是第一章了！！！")
        }
        popMenuView.next_chapter.setOnClickListener {
            if (nextChapter != directory) {
                loadingLayout.showLoading()
                textBody.setLength(0)
                thisLink = nextChapter!!
                oldChapter = ""
                scrollView.scrollTo(0, 0)
                initData(nextChapter!!)
            } else
                toast("已经是最后一章了！！！")
        }
        if (variable == "") variable = "red"
        when (variable) {
            "red" -> {
                popMenuView.e_r.isChecked = true
                main.backgroundColor = resources.getColor(R.color.eye_red)
                bColor = resources.getColor(R.color.eye_red)
            }
            "green" -> {
                popMenuView.e_gen.isChecked = true
                main.backgroundColor = resources.getColor(R.color.eye_green)
                bColor = resources.getColor(R.color.eye_green)
            }
            "white" -> {
                popMenuView.e_w.isChecked = true
                main.backgroundColor = resources.getColor(R.color.eye_white)
                bColor = resources.getColor(R.color.eye_white)
            }
            "blue" -> {
                popMenuView.e_b.isChecked = true
                main.backgroundColor = resources.getColor(R.color.eye_blue)
                bColor = resources.getColor(R.color.eye_blue)
            }
            "gray" -> {
                popMenuView.e_g.isChecked = true
                main.backgroundColor = resources.getColor(R.color.eye_grey)
                bColor = resources.getColor(R.color.eye_grey)
            }
        }
        popMenuView.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.e_w -> {
                    main.backgroundColor = resources.getColor(R.color.eye_white)
                    bColor = resources.getColor(R.color.eye_white)
                    variable = "white"
                }
                R.id.e_gen -> {
                    main.backgroundColor = resources.getColor(R.color.eye_green)
                    bColor = resources.getColor(R.color.eye_green)
                    variable = "green"
                }
                R.id.e_g -> {
                    main.backgroundColor = resources.getColor(R.color.eye_grey)
                    bColor = resources.getColor(R.color.eye_grey)
                    variable = "gray"
                }
                R.id.e_b -> {
                    main.backgroundColor = resources.getColor(R.color.eye_blue)
                    bColor = resources.getColor(R.color.eye_blue)
                    variable = "blue"
                }
                R.id.e_r -> {
                    main.backgroundColor = resources.getColor(R.color.eye_red)
                    bColor = resources.getColor(R.color.eye_red)
                    variable = "red"
                }
            }
        }
        main.setOnClickListener {
            if (isShow) {
                popTitle.dismiss()
                popMenu.dismiss()
                full(true)
            } else {
                popTitle.showAtLocation(it, Gravity.TOP, 0, 64)
                popTitle.update()

                popMenu.showAtLocation(it, Gravity.BOTTOM, 0, 0)
                popMenu.update()
                full(false)
            }
            isShow = !isShow
        }
        text_tv.setOnClickListener {
            if (isShow) {
                popTitle.dismiss()
                popMenu.dismiss()
                full(true)
            } else {
                popTitle.showAtLocation(it, Gravity.TOP, 0, 64)
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
            attr.flags = attr.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            window.attributes = attr
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    fun clickChapter(link: String) {
        loadingLayout.showLoading()
        textBody.setLength(0)
        scrollView.scrollTo(0, 0)
        thisLink = link
        initData(link)
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
                upChapter = doc.getElementsByClass("page")[0].select("a").first().attr("abs:href")
                nextChapter = doc.getElementsByClass("page")[0].select("a").last().attr("abs:href")
                name.text = doc.getElementsByClass("book_title").text()
                text_tv.text = Html.fromHtml(textBody.toString(), imageGetter, null)
                if (isFirst) {
                    initDirectory(directory)
                    isFirst = false
                }
            }
        }
    }

    private fun initDirectory(link: String) {
        doAsync {
            var doc = Jsoup.connect(link).get()
            uiThread {
                bName = doc.select("img").attr("alt")
                var elements = doc.getElementsByClass("ocon").select("a")
                for (element in elements) {
                    var title = element.attr("title")
                    var link = element.attr("abs:href")
                    chapters.add(Chapter(title, link))
                }
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
