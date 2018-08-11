package lyh.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_search_name.view.*
import lyh.readdemo.R
import lyh.readdemo.SearchActivity
import lyh.util.database
import lyh.util.sName

class SearchNameAdapter(var content: Context?, var sNames: ArrayList<sName>) : BaseAdapter() {
    override fun getItem(position: Int): Any = sNames[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = sNames.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(content, R.layout.item_search_name, null)
            holder = ViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as ViewHolder
        }

        holder.name.text = sNames[position].name
        holder.del.setOnClickListener {
            content!!.database.use {
                execSQL("delete from searchName where name='" + sNames[position].name + "'")
                SearchActivity.instance!!.getData()
            }
        }
        return v
    }

    class ViewHolder(v: View) {
        var name: TextView = v.name
        var del: ImageView = v.del
    }
}