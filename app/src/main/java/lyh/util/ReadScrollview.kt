package lyh.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView

class ReadScrollview : ScrollView {

    private var scrollViewListener: ScrollViewToBottomListener? = null
    private var onScrollViewToBottomLiatener: OnScrollViewToBottomLiatener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun setScrollViewListener(scrollViewListener: ScrollViewToBottomListener) {
        this.scrollViewListener = scrollViewListener
    }

    fun setOnScrollViewToBottomLiatener(onScrollViewToBottomLiatener: OnScrollViewToBottomLiatener) {
        this.onScrollViewToBottomLiatener = onScrollViewToBottomLiatener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val view = getChildAt(childCount - 1) as View

        var d = view.getBottom()

        //根据距离判断是否滑到了底部
        d -= height + scrollY

        //        Log.e("---------->","d"+d);
        if (d == 0) {
            //滑到底部的监听
            if (onScrollViewToBottomLiatener != null) {
                onScrollViewToBottomLiatener!!.onScrollViewToBottomListener()
            }

        } else {
            //滑动监听，可以根据滑动的距离做相应的事件，如返回顶部
            if (scrollViewListener != null) {
                scrollViewListener!!.onScrollChanged(this, l, t, oldl, oldt)
            }
        }
    }

    interface ScrollViewToBottomListener {
        fun onScrollChanged(scrollView: ReadScrollview,
                            x: Int, y: Int, oldx: Int, oldy: Int)
    }

    interface OnScrollViewToBottomLiatener {
        fun onScrollViewToBottomListener()
    }


}