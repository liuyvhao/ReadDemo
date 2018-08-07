package lyh.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_collect.view.*
import lyh.readdemo.R
import lyh.util.Collect
import lyh.util.OnItemClickLitener

class CollectAdapter(var mOnItemClickLitener: OnItemClickLitener, var collectList: ArrayList<Collect>) : RecyclerView.Adapter<CollectAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CollectAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_collect, parent, false))
    override fun getItemCount() = collectList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.img.setImageURI(collectList[position].img)
        holder.itemView.name.text = collectList[position].name
        holder.itemView.setOnClickListener {
            mOnItemClickLitener.onItemClick(holder.itemView, holder.layoutPosition)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
