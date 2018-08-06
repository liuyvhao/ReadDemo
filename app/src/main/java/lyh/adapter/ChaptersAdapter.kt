package lyh.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.item_chapter.view.*
import lyh.readdemo.R
import lyh.util.Chapter

class ChaptersAdapter(var content: Context?, var chapterList: ArrayList<Chapter>) : BaseAdapter() {
    override fun getItem(position: Int): Any = chapterList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = chapterList.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(content, R.layout.item_chapter, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.chapter.text = chapterList[position].title

        return v
    }

    class ViewHolder(v: View) {
        var chapter: TextView = v.chapter
    }
}