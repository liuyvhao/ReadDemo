package lyh.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_science.view.*
import lyh.adapter.BookListAdapter
import lyh.readdemo.BookInfoActivity
import lyh.readdemo.R
import lyh.util.BookList
import lyh.util.Head
import lyh.util.OnItemClickLitener
import lyh.util.SpaceItemDecoration
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.startActivityForResult
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup

/**
 * 科幻小说
 */
class ScienceFragment : Fragment(), OnItemClickLitener {
    private var v: View? = null
    private var bookLists = ArrayList<BookList>()
    private var adapter: BookListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_science, container, false)
        initView()
        initData()
        return v
    }

    private fun initView() {
        adapter = BookListAdapter(this, bookLists)
        v!!.recyclerView.layoutManager = LinearLayoutManager(v!!.context)
        v!!.recyclerView.addItemDecoration(SpaceItemDecoration(10))
        v!!.recyclerView.adapter = adapter
        v!!.swipeRefresh.setColorScheme(R.color.colorAccent)
        v!!.swipeRefresh.setOnRefreshListener {
            bookLists.clear()
            initData()
        }
    }

    override fun onItemClick(view: View, position: Int) {
        startActivityForResult<BookInfoActivity>(1,"link" to Head.urlHead +bookLists[position].link)
    }

    private fun initData() {
        v!!.swipeRefresh.isRefreshing = true
        doAsync {
            var doc = Jsoup.connect(Head.urlHead + "/modules/article/articlelist.php?class=7").get()
            uiThread {
                Head.analysis(doc, bookLists)
                adapter?.notifyDataSetChanged()
                v!!.swipeRefresh.isRefreshing = false
            }
        }
    }

}