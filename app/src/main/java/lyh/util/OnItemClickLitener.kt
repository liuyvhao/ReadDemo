package lyh.util

import android.view.View

interface OnItemClickLitener {
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View, position: Int){}
}