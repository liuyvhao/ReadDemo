package lyh.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_end.view.*
import lyh.adapter.SearchListAdapter
import lyh.readdemo.BookInfoActivity
import lyh.readdemo.R
import lyh.readdemo.SearchActivity
import lyh.util.BookList
import lyh.util.Head
import lyh.util.OnItemClickLitener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup

/**
 * 搜索完结
 */
class EndFragment : Fragment(), OnItemClickLitener {
    private var v: View? = null
    private var bookLists = ArrayList<BookList>()
    private var adapter: SearchListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_end, container, false)
        initView()
        return v
    }

    private fun initView() {
        adapter = SearchListAdapter(this, bookLists)
        v!!.recyclerView.layoutManager = LinearLayoutManager(v!!.context)
        v!!.recyclerView.adapter = adapter
        v!!.swipeRefresh.setColorScheme(R.color.colorAccent)
        v!!.swipeRefresh.setOnRefreshListener {
            bookLists.clear()
            initData()
        }
    }

    override fun onItemClick(view: View, position: Int) {
        startActivity<BookInfoActivity>("link" to bookLists[position].link)
    }

    fun initData() {
        v!!.swipeRefresh.isRefreshing = true
        doAsync {
            var doc = Jsoup.connect("http://zhannei.baidu.com/cse/search?q=" + SearchActivity.bookName + "&entry=1&s=11488190379777616912&flt=updateStatus%3DupdateStatus%253A%25E5%25AE%258C%25E7%25BB%2593&nsid=0").get()
            uiThread {
                Head.analysisSearch(doc, bookLists)
                adapter?.notifyDataSetChanged()
                v!!.swipeRefresh.isRefreshing = false
            }
        }
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                1 -> {
                    bookLists.clear()
                    initData()
                }
            }
        }
    }

}