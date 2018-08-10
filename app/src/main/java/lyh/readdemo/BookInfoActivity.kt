package lyh.readdemo

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_book_info.*
import lyh.util.Chapter
import lyh.util.database
import org.jetbrains.anko.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jsoup.Jsoup

/**
 * 书籍详情
 */
class BookInfoActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var chapters = ArrayList<Chapter>()
    }

    private lateinit var bName: String
    private lateinit var bImg: String
    private lateinit var startLink: String
    private lateinit var newLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var local = window.attributes
            local.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        }
        setContentView(R.layout.activity_book_info)
        initView()
        initData()
    }

    fun initView() {
        back.setOnClickListener(this)
        directory.setOnClickListener { startActivityForResult<ReadActivity>(1,"link" to newLink, "name" to bName, "img" to bImg) }
        chapters.clear()
        collect.setOnClickListener {
            database.use {
                insert("collectBook",
                        "name" to bName,
                        "img" to bImg,
                        "link" to startLink,
                        "time" to System.currentTimeMillis().toString())
                collect.isClickable = false
                collect.setBackgroundColor(resources.getColor(R.color.LightGrey))
            }
        }
        read.setOnClickListener { startActivityForResult<DirectoryActivity>(1,"name" to bName, "img" to bImg) }
    }

    private fun initData() {
        doAsync {
            var doc = Jsoup.connect(intent.getStringExtra("link")).get()
            uiThread {
                loadingLayout.showContent()
                type.text = doc.getElementsByClass("class_name").text()
                bImg = doc.select("img").attr("src")
                img.setImageURI(bImg)
                bName = doc.select("img").attr("alt")
                name.text = bName
                author.text = doc.getElementsByClass("title").select("span").text()
                introduction.text = doc.getElementsByClass("introCon").text()
                newChapter.text = doc.getElementsByClass("n").text()
                newLink = doc.getElementsByClass("n")[0].select("a").attr("abs:href")
                var elements = doc.getElementsByClass("ocon").select("a")
                startLink = elements.first().attr("abs:href")
                for (element in elements) {
                    var title = element.attr("title")
                    var link = element.attr("abs:href")
                    chapters.add(Chapter(title, link))
                }
                database.use {
                    select("collectBook").whereSimple("name=?", bName).exec {
                        if (count != 0) {
                            collect.isClickable = false
                            collect.setBackgroundColor(resources.getColor(R.color.LightGrey))
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back -> finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        database.use {
            select("collectBook").whereSimple("name=?", bName).exec {
                if (count != 0) {
                    collect.isClickable = false
                    collect.setBackgroundColor(resources.getColor(R.color.LightGrey))
                }
            }
        }
    }
}
