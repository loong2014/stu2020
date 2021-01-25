package com.sunny.module.account

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zhangxin17 on 2020/12/29
 */

class LoginStatus {
    companion object {
        const val UnLogin = -1
        const val Login = 1
        const val VIP = 2
    }
}

/**
 * 声明为一个实体类
 */
@Entity
data class UserModel(var userId: Long = -1, var name: String? = null, var status: Int = LoginStatus.UnLogin) {

    /**
     * 设置id为主键，自增长
     */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}