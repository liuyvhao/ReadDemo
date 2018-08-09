package lyh.readdemo

import android.app.TabActivity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.RadioGroup
import android.widget.TabHost
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : TabActivity(), RadioGroup.OnCheckedChangeListener {
    lateinit var mTabHost: TabHost
    //点击关闭的初始时间
    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        //实例化选项卡
        mTabHost = this.tabHost
        //添加选项卡
        mTabHost?.addTab(mTabHost?.newTabSpec("book")?.setIndicator("book")
                ?.setContent(Intent(this, BookActivity::class.java)))

        mTabHost?.addTab(mTabHost?.newTabSpec("bookshelf")?.setIndicator("bookshelf")
                ?.setContent(Intent(this, BookShelfActivity::class.java)))

        mTabHost?.addTab(mTabHost?.newTabSpec("bookhot")?.setIndicator("bookhot")
                ?.setContent(Intent(this, BookHotActivity::class.java)))

        radiogroup.setOnCheckedChangeListener(this)
        radiogroup.check(R.id.book)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.book -> {
                mTabHost?.setCurrentTabByTag("book")
            }
            R.id.bookshelf -> {
                mTabHost?.setCurrentTabByTag("bookshelf")
            }
            R.id.bookhot -> {
                mTabHost?.setCurrentTabByTag("bookhot")
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK
                && event.action == KeyEvent.ACTION_DOWN
                && event.repeatCount == 0) {
            //具体的操作代码
            if (System.currentTimeMillis() - exitTime > 2000) {
                toast("再次点击退出！")
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
        }
        return true
    }
}
