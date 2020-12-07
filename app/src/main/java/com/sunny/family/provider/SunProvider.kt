package com.sunny.family.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.sunny.family.db.SunnyDatabaseHelper


/**
 * Created by zhangxin17 on 2020/12/7
 * 将数据提供给第三方
 */
class SunProvider : ContentProvider() {

    companion object {
        private const val Authority = "com.sunny.stu.provider"
    }

    private val uriMatcher by lazy {
        UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("com.sunny.stu.provider", "book", bookDir) // * 表示匹配任意长度的任意字符
            addURI("com.sunny.stu.provider", "book/#", bookItem) // # 表示匹配任意长度的数字
        }
    }

    private val bookDir = 1 // 查询所有数据
    private val bookItem = 2 // 查询单条记录

    private var dbHelper: SunnyDatabaseHelper? = null

    override fun onCreate(): Boolean {
        return context?.let {
            dbHelper = SunnyDatabaseHelper.getDefaultDB(it)
            true
        } ?: false
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        bookDir -> "vnd.android.cursor.dir/vnd.com.sunny.stu.provider.book"
        bookItem -> "vnd.android.cursor.item/vnd.com.sunny.stu.provider.book"
        else -> null
    }

    override fun query(uri: Uri, projection: Array<out String>?,
                       selection: String?, selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor? = dbHelper?.let {
        val db = it.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            // 查询所有数据
            bookDir -> {
                db.query("Book", projection, selection, selectionArgs, null, null, sortOrder)
            }

            // 查询单条记录
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.query("Book", projection, "id = ?", arrayOf(bookId), null, null, sortOrder)
            }
            else -> null
        }
        cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = dbHelper?.let {
        val db = it.writableDatabase
        val uriReturn = when (uriMatcher.match(uri)) {

            bookDir, bookItem -> {
                val newBookId = db.insert("Book", null, values)
                Uri.parse("content://$Authority/book/$newBookId")
            }
            else -> null
        }
        uriReturn
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = dbHelper?.let {

        val db = it.writableDatabase
        val deleteRows = when (uriMatcher.match(uri)) {
            bookDir -> db.delete("Book", selection, selectionArgs)
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.delete("Book", "id = ?", arrayOf(bookId))
            }
            else -> 0
        }
        deleteRows
    } ?: 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = dbHelper?.let {
        val db = it.writableDatabase
        val updateRows = when (uriMatcher.match(uri)) {
            bookDir -> {
                db.update("Book", values, selection, selectionArgs)
            }
            bookItem -> {
                val bookId = uri.pathSegments[1]
                db.update("Book", values, "id = ?", arrayOf(bookId))
            }
            else -> 0
        }
        updateRows
    } ?: 0

}