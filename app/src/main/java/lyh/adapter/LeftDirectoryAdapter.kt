package lyh.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_read_directory.view.*
import lyh.readdemo.R
import lyh.util.Chapter
import org.jetbrains.anko.textColor

class LeftDirectoryAdapter(var content: Context?, var chapterList: ArrayList<Chapter>) : BaseAdapter() {
    override fun getItem(position: Int): Any = chapterList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = chapterList.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(content, R.layout.item_read_directory, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.chapter.text = chapterList[position].title
        if (chapterList[position].check) {
            holder.chapter.textColor = content!!.resources.getColor(R.color.red)
            holder.dian.setImageResource(R.drawable.left_d_r)
        } else {
            holder.chapter.textColor = content!!.resources.getColor(android.R.color.black)
            holder.dian.setImageResource(R.drawable.left_d_b)
        }
        return v
    }

    class ViewHolder(v: View) {
        var chapter: TextView = v.chapter
        var dian: ImageView = v.dian
    }
}