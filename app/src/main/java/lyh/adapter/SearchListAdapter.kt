package lyh.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_booklist.view.*
import lyh.readdemo.R
import lyh.util.BookList
import lyh.util.OnItemClickLitener

class SearchListAdapter(var mOnItemClickLitener: OnItemClickLitener, var bookLists: ArrayList<BookList>) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchListAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_searhlisth, parent, false))

    override fun getItemCount(): Int = bookLists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.img.setImageURI(bookLists[position].img)
        holder.itemView.title.text = bookLists[position].title
        holder.itemView.introduction.text = bookLists[position].introduction
        holder.itemView.author.text = bookLists[position].author
        holder.itemView.chapter.text = bookLists[position].chapter
        holder.itemView.setOnClickListener {
            mOnItemClickLitener.onItemClick(holder.itemView, holder.layoutPosition)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}