package lyh.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_history.view.*
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
 * 历史军事
 */
class HistoryFragment : Fragment(), OnItemClickLitener {
    private var v: View? = null
    private var bookLists = ArrayList<BookList>()
    private var adapter: BookListAdapter? = null
    var thisLink = Head.urlHead + "/modules/article/articlelist.php?class=4"
    var nextLink: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_history, container, false)
        initView()
        initData(thisLink)
        return v
    }

    private fun initView() {
        adapter = BookListAdapter(this, bookLists)
        v!!.recyclerView.layoutManager = LinearLayoutManager(v!!.context)
        v!!.recyclerView.addItemDecoration(SpaceItemDecoration(10))
        v!!.recyclerView.adapter = adapter
        v!!.swipeRefresh.refreshHeader = ClassicsHeader(v!!.context).setSpinnerStyle(SpinnerStyle.Translate).setEnableLastTime(false)
        v!!.swipeRefresh.refreshFooter = ClassicsFooter(v!!.context).setSpinnerStyle(SpinnerStyle.Translate)
        v!!.swipeRefresh.setOnRefreshListener {
            bookLists.clear()
            initData(thisLink)
        }
        v!!.swipeRefresh.setOnLoadmoreListener {
            initData(nextLink!!)
        }
    }

    override fun onItemClick(view: View, position: Int) {
        startActivityForResult<BookInfoActivity>(1, "link" to Head.urlHead + bookLists[position].link)
    }

    private fun initData(link: String) {
        doAsync {
            var doc = Jsoup.connect(link).get()
            uiThread {
                v!!.loadingLayout.showContent()
                nextLink = doc.getElementsByClass("next").attr("abs:href")
                Head.analysis(doc, bookLists)
                adapter?.notifyDataSetChanged()
                if (v!!.swipeRefresh.isRefreshing)
                    v!!.swipeRefresh.finishRefresh(0)
                if (v!!.swipeRefresh.isLoading)
                    v!!.swipeRefresh.finishLoadmore(0)
            }
        }
    }

}