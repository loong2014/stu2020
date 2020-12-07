package com.sunny.family.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sunny.lib.utils.SunLog


/**
 * Created by zhangxin17 on 2020/12/8
 * 数据库
 */
class SunnyDatabaseHelper(context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    companion object {
        private const val TAG = "Sunny-DB";

        fun getDefaultDB(context: Context): SunnyDatabaseHelper {
            return SunnyDatabaseHelper(context, "BookStore.db", 1)
        }
    }

    private val createBook = "create table Book (" +
            " id integer primary key autoincrement," +
            "time integer," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text )"

    private val createCategory = "create table Category (" +
            " id integer primary key autoincrement," +
            "name text," +
            "code integer )"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createBook)
        SunLog.i(TAG, "onCreate Book 表创建成功")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        SunLog.i(TAG, "onUpgrade :$oldVersion >>> $newVersion")

        if (oldVersion <= 1) {
            db.execSQL(createCategory)
        }

        if (oldVersion <= 2) {
            db.execSQL("alter table Book add column category_id integer")
        }
    }
}