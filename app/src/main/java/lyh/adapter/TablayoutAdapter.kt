package lyh.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TablayoutAdapter : FragmentPagerAdapter {
    private var fragments: List<Fragment>? = null
    private var titles: Array<String>? = null

    constructor(fm: FragmentManager, fragments: List<Fragment>, titles: Array<String>) : super(fm) {
        this.fragments = fragments
        this.titles = titles
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles!![position]
    }

    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getCount(): Int {
        return fragments!!.size
    }
}