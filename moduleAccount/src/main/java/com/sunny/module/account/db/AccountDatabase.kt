package com.sunny.module.account.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sunny.module.account.UserModel

/**
 * Created by zhangxin17 on 2021/1/25
 */
@Database(version = 1, entities = [UserModel::class])
abstract class AccountDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var instance: AccountDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AccountDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                    AccountDatabase::class.java, "account_database")
                    .build().apply {
                        instance = this
                    }
        }
    }
}