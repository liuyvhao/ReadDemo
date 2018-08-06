package lyh.readdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.androidkun.xtablayout.XTabLayout
import kotlinx.android.synthetic.main.activity_search.*
import lyh.adapter.TablayoutAdapter
import lyh.fragment.AllFragment
import lyh.fragment.EndFragment
import java.util.ArrayList

/**
 * 搜索
 */
class SearchActivity : AppCompatActivity(), XTabLayout.OnTabSelectedListener {
    private var titles = arrayOf("全部", "完结")
    private val fragments = ArrayList<Fragment>()
    private var allFragment = AllFragment()
    private var endFragment = EndFragment()

    companion object {
        var bookName = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
    }

    fun initView() {
        back.setOnClickListener { finish() }
        search.setOnClickListener { initData() }
        search_edit.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                initData()
                return@OnEditorActionListener true
            }
            false
        })
        for (item in titles)
            tabLayout.addTab(tabLayout.newTab().setText(item))
        tabLayout.setOnTabSelectedListener(this)
        fragments.add(allFragment)
        fragments.add(endFragment)
        view_pager.adapter = TablayoutAdapter(supportFragmentManager, fragments, titles)
        tabLayout.setupWithViewPager(view_pager)
        tabLayout.visibility = View.GONE
    }

    fun initData() {
        bookName = search_edit.text.toString()
        tabLayout.visibility = View.VISIBLE
        allFragment.handler.sendEmptyMessage(1)
        endFragment.handler.sendEmptyMessage(1)
    }

    override fun onTabReselected(tab: XTabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: XTabLayout.Tab?) {
    }

    override fun onTabSelected(tab: XTabLayout.Tab?) {
        if (tab != null) view_pager.currentItem = tab.position
    }
}
