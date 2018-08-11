package lyh.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class BookSqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "xBook.db") {
    companion object {
        var instance: BookSqlHelper? = null
        fun getInstance(ctx: Context): BookSqlHelper {
            if (instance == null)
                instance = BookSqlHelper(ctx.applicationContext)
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("collectBook", true,
                "name" to TEXT,
                "img" to TEXT,
                "link" to TEXT,
                "time" to TEXT)
        db.createTable("searchName", true,
                "name" to TEXT,
                "time" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("xBook.db", true)
    }

}

val Context.database: BookSqlHelper
    get() = BookSqlHelper.getInstance(applicationContext)