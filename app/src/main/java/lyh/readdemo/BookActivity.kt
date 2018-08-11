package lyh.readdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.item_collect.view.*
import lyh.adapter.CollectAdapter
import lyh.util.Collect
import lyh.util.OnItemClickLitener
import lyh.util.database
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivityForResult

/**
 * 书架
 */
class BookActivity : AppCompatActivity(), OnItemClickLitener {
    companion object {
        var instance: BookActivity? = null
    }

    private var collectList = ArrayList<Collect>()
    var adapter: CollectAdapter? = null
    private var isMove = false
    private var delList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        instance = this
        initView()
        initData()
    }

    fun initView() {
        adapter = CollectAdapter(this, collectList)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        all.setOnClickListener {
            when (all.text) {
                "全选" -> {
                    delList.clear()
                    for (i in collectList.indices) {
                        delList.add(i)
                        collectList[i].check = true
                    }
                    all.text = "取消"
                    del.setTextColor(resources.getColor(R.color.red))
                    del.isClickable = true
                }
                "取消" -> {
                    delList.clear()
                    for (i in collectList.indices) {
                        collectList[i].check = false
                    }
                    all.text = "全选"
                    del.setTextColor(resources.getColor(R.color.line))
                    del.isClickable = false
                }
            }
            adapter?.notifyDataSetChanged()
        }
        del.setOnClickListener {
            AlertDialog.Builder(this)
                    .setMessage("删除图书")
                    .setPositiveButton("确定") { _, _ ->
                        for (i in delList) {
                            database.use {
                                execSQL("delete from collectBook where name='" + collectList[i].name + "'")
                            }
                        }
                        isMove = false
                        delList.clear()
                        resData()
                        del.visibility = View.GONE
                        all.visibility = View.GONE
                        cancel.visibility = View.GONE
                    }
                    .setNegativeButton("取消") { _, _ ->

                    }.create().show()
        }
        cancel.setOnClickListener {
            isMove = false
            delList.clear()
            for (i in collectList.indices) {
                collectList[i].check = false
            }
            adapter?.notifyDataSetChanged()
            all.text = "全选"
            del.visibility = View.GONE
            all.visibility = View.GONE
            cancel.visibility = View.GONE
        }
    }

    override fun onItemClick(view: View, position: Int) {
        if (isMove) {
            del.setTextColor(resources.getColor(R.color.red))
            del.isClickable = true
            collectList[position].check = !collectList[position].check
            if (collectList[position].check)
                delList.add(position)
            else
                delList.remove(position)
            if (delList.size == 0) {
                del.setTextColor(resources.getColor(R.color.line))
                del.isClickable = false
            }
            adapter?.notifyDataSetChanged()
        } else
            startActivityForResult<ReadActivity>(1, "link" to collectList[position].link,
                    "name" to collectList[position].name, "img" to collectList[position].img)
    }

    override fun onItemLongClick(view: View, position: Int) {
        if (!view.cb.isChecked) {
            isMove = true
            collectList[position].check = true
            delList.add(position)
            all.visibility = View.VISIBLE
            del.visibility = View.VISIBLE
            del.setTextColor(resources.getColor(R.color.red))
            del.isClickable = true
            cancel.visibility = View.VISIBLE
            adapter?.notifyDataSetChanged()
        }
    }

    fun resData() {
        collectList.clear()
        initData()
    }

    fun initData() {
        database.use {
            var books = select("collectBook").orderBy("time", SqlOrderDirection.DESC).parseList(object : MapRowParser<Collect> {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resData()
    }
}
