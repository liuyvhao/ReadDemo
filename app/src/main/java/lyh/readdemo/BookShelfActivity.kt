package lyh.readdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_book_shelf.*
import lyh.adapter.TablayoutAdapter
import lyh.fragment.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import java.util.ArrayList

/**
 * 书库
 */
class BookShelfActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    var titles = arrayOf("玄幻魔法", "武侠修真", "都市言情", "历史军事", "网游竞技", "科幻小说")
    private val fragments = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_shelf)
        initView()
    }

    private fun initView() {
        search.setOnClickListener { startActivityForResult<SearchActivity>(1) }
        for (item in titles)
            tabLayout.addTab(tabLayout.newTab().setText(item))
        tabLayout.setOnTabSelectedListener(this)
        fragments.add(FantasyFragment())
        fragments.add(MartialArtsFragment())
        fragments.add(CityFragment())
        fragments.add(HistoryFragment())
        fragments.add(SportsFragment())
        fragments.add(ScienceFragment())
        view_pager.adapter = TablayoutAdapter(supportFragmentManager, fragments, titles)
        tabLayout.setupWithViewPager(view_pager)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) view_pager.currentItem = tab.position
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        BookActivity.instance?.resData()
    }
}
