package lyh.readdemo

import android.content.Context
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
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


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
        var instance: SearchActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        instance = this
        initView()
    }

    fun initView() {
        back.setOnClickListener { finish() }
        search.setOnClickListener {
            // 先隐藏键盘
            (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(this
                            .currentFocus!!
                            .windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS)
            initData()
        }
        search_edit.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 先隐藏键盘
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(this
                                .currentFocus!!
                                .windowToken,
                                InputMethodManager.HIDE_NOT_ALWAYS)
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
        allFragment.handler.sendEmptyMessage(1)
        endFragment.handler.sendEmptyMessage(1)
    }

    fun showTab() {
        tabLayout.visibility = View.VISIBLE
    }

    override fun onTabReselected(tab: XTabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: XTabLayout.Tab?) {
    }

    override fun onTabSelected(tab: XTabLayout.Tab?) {
        if (tab != null) view_pager.currentItem = tab.position
    }
}
