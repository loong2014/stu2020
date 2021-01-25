package com.sunny.module.account.db

import androidx.room.*
import com.sunny.module.account.UserModel

/**
 * Created by zhangxin17 on 2021/1/25
 */
@Dao
interface UserDao {

    @Insert
    fun insertUser(userModel: UserModel)

    @Delete
    fun deleteUser(userModel: UserModel)

    @Update
    fun updateUser(userModel: UserModel)

    @Query("select * from UserModel")
    fun loadAllUsers(): List<UserModel>

    @Query("select * from UserModel where userId = :userId ")
    fun loadUser(userId: Long): UserModel

}