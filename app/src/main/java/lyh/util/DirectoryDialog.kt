package lyh.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import kotlinx.android.synthetic.main.read_directory.*
import lyh.adapter.LeftDirectoryAdapter
import lyh.readdemo.R
import lyh.readdemo.ReadActivity
import org.jetbrains.anko.backgroundColor

/**
 * 阅读页面左侧目录
 */
class DirectoryDialog(context: Context, var bName: String, var chapters: ArrayList<Chapter>, var color: Int, var cName: String) : Dialog(context, R.style.DirectoryDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_directory)
        window.setGravity(Gravity.LEFT)
        val lp = window!!.attributes
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = lp
        viewBg.backgroundColor = color
        list_item.backgroundColor = color
        name.text = bName
        var p: Int? = null
        for (i in chapters.indices) {
            if (chapters[i].title == cName) {
                chapters[i].check = true
                p = i
            } else
                chapters[i].check = false
        }
        list_item.adapter = LeftDirectoryAdapter(context, chapters)
        list_item.setSelection(p!!)
        list_item.setOnItemClickListener { _, _, position, _ ->
            ReadActivity.instance?.clickChapter(chapters[position].link)
            dismiss()
        }
    }
}