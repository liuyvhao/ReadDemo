package lyh.readdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_book.*
import lyh.adapter.CollectAdapter
import lyh.util.Collect
import lyh.util.OnItemClickLitener
import lyh.util.database
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity

/**
 * 书架
 */
class BookActivity : AppCompatActivity(), OnItemClickLitener {
    var collectList = ArrayList<Collect>()
    var adapter: CollectAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        initView()
        initData()
    }

    fun initView() {
        adapter = CollectAdapter(this, collectList)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, position: Int) {
        startActivity<ReadActivity>("link" to collectList[position].link, "name" to collectList[position].name, "img" to collectList[position].img)
    }


    fun initData() {
        database.use {
            var books = select("collectBook").orderBy("time").parseList(object : MapRowParser<Collect> {
                override fun parseRow(columns: Map<String, Any?>): Collect {
                    var name = columns["name"] as String
                    var img = columns["img"] as String
                    var link = columns["link"] as String
                    var time = columns["time"] as String
                    return Collect(name, img, link, time)
                }
            })
            for (book in books) {
                collectList.add(book)
            }
            adapter?.notifyDataSetChanged()

        }
    }
}
