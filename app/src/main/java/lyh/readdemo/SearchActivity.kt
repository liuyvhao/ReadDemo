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
import android.view.inputmethod.InputMethodManager
import lyh.adapter.SearchNameAdapter
import lyh.util.database
import lyh.util.sName
import org.jetbrains.anko.db.*
import org.jetbrains.anko.toast

/**
 * 搜索
 */
class SearchActivity : AppCompatActivity(), XTabLayout.OnTabSelectedListener {
    private var titles = arrayOf("全部", "完结")
    private val fragments = ArrayList<Fragment>()
    private var allFragment = AllFragment()
    private var endFragment = EndFragment()
    private var sNames = ArrayList<sName>()
    var adapter: SearchNameAdapter? = null

    companion object {
        var bookName = ""
        var instance: SearchActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        instance = this
        initView()
        getData()
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
            initData(search_edit.text.toString())
        }
        search_edit.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // 先隐藏键盘
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(this
                                .currentFocus!!
                                .windowToken,
                                InputMethodManager.HIDE_NOT_ALWAYS)
                initData(search_edit.text.toString())
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
        adapter = SearchNameAdapter(this, sNames)
        list_item.adapter = adapter
        list_item.setOnItemClickListener { _, _, position, _ ->
            initData(sNames[position].name)
        }
    }

    fun getData() {
        sNames.clear()
        database.use {
            var names = select("searchName").orderBy("time", SqlOrderDirection.DESC).parseList(object : MapRowParser<sName> {
                override fun parseRow(columns: Map<String, Any?>): sName {
                    var name = columns["name"] as String
                    var time = columns["time"] as String
                    return sName(name, time)
                }
            })
            for (name in names) {
                sNames.add(name)
            }
            adapter?.notifyDataSetChanged()
        }
    }

    private fun addData(name: String) {
        database.use {
            select("searchName").whereSimple("name=?", name).exec {
                if (count == 0)
                    insert("searchName",
                            "name" to name,
                            "time" to System.currentTimeMillis().toString())
                else
                    update("searchName",
                            "time" to System.currentTimeMillis().toString()).whereSimple("name=?", name).exec()
            }
        }
    }

    fun initData(name: String) {
        if (name.trim() == "")
            toast("请输入书名！")
        else {
            addData(name)
            list_item.visibility = View.GONE
            bookName = name
            allFragment.handler.sendEmptyMessage(1)
            endFragment.handler.sendEmptyMessage(1)
        }
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
