package lyh.readdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_directory.*
import lyh.adapter.ChaptersAdapter
import lyh.util.Chapter
import org.jetbrains.anko.startActivity

/**
 * 目录
 */
class DirectoryActivity : AppCompatActivity() {
    private var chapters = ArrayList<Chapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        initView()
    }

    fun initView() {
        back.setOnClickListener { finish() }
        chapters.clear()
        chapters = BookInfoActivity.chapters
        chapters_tv.text = "共" + chapters.size + "章"
        list_item.adapter = ChaptersAdapter(this, chapters)
        list_item.setOnItemClickListener { _, _, position, _ ->
            startActivity<ReadActivity>("link" to chapters[position].link,"name" to intent.getStringExtra("name"),"img" to intent.getStringExtra("img")) }
    }
}
