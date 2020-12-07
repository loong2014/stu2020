package com.sunny.family.db

import android.content.ContentValues
import android.os.Bundle
import androidx.core.content.contentValuesOf
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.act_database.*

/**
 * Created by zhangxin17 on 2020/12/8
 */
@Route(path = RouterConstant.PageDatabase)
class DatabaseActivity : BaseActivity() {

    companion object {
        private const val TAG = "Sunny-DB"
    }

    private val dbHelper by lazy {
        SunLog.i(TAG, "dbHelper init")
        SunnyDatabaseHelper.getDefaultDB(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_database)

        initView()
    }

    private fun initView() {
        btn_db_create.setOnClickListener {
            SunLog.i(TAG, "btn_db_create")
            dbHelper.writableDatabase
        }

        btn_db_insert.setOnClickListener {
            SunLog.i(TAG, "btn_db_insert")

            val db = dbHelper.writableDatabase

            val value1 = ContentValues().apply {
                put("name", "ABC")
                put("author", "张三")
                put("pages", 26)
                put("price", 123)
                put("time", System.currentTimeMillis())
            }
            db.insert("Book", null, value1)

            //
            val value2 = contentValuesOf("name" to "AAA", "author" to "Sunny",
                    "pages" to 720, "price" to 1.23, "time" to System.currentTimeMillis())

            db.insert("Book", null, value2)

        }
        btn_db_update.setOnClickListener {
            SunLog.i(TAG, "btn_db_update")

            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("price", 456)
            }

            db.update("Book", values, "name = ?", arrayOf("ABC"))

        }

        btn_db_delete.setOnClickListener {
            SunLog.i(TAG, "btn_db_delete")

            val db = dbHelper.writableDatabase
            db.beginTransaction()

            db.delete("Book", "pages = ?", arrayOf("26"))

            db.endTransaction()
        }

        btn_db_query.setOnClickListener {
            SunLog.i(TAG, "btn_db_query")

            val db = dbHelper.writableDatabase

            val cursor = db.query("Book", null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("name"))
                    val price = cursor.getDouble(cursor.getColumnIndex("price"))
                    val time = cursor.getLong(cursor.getColumnIndex("time"))

                    SunLog.i(TAG, "cursor : $name , $price , $time")

                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

    private fun cvOf(vararg pairs: Pair<String, Any?>) = ContentValues().apply {

        for (pair in pairs) {
            val key = pair.first
            val value = pair.second
            when (value) {
                is Int -> put(key, value)
                is Long -> put(key, value)
                is Short -> put(key, value)
                is Float -> put(key, value)
                is Double -> put(key, value)
                is Boolean -> put(key, value)
                is String -> put(key, value)
                is Byte -> put(key, value)
                is ByteArray -> put(key, value)
                null -> putNull(key)
            }
        }
    }

}