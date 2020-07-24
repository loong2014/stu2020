package com.sunny.family.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * Created by zhangxin17 on 2020/7/14
 */
data class User(val name: String, val uid: Long)

class UserViewModel : ViewModel() {
    val userIdLiveData = MutableLiveData<Long>()
    val userLiveData: LiveData<User> = Transformations
            .map(userIdLiveData) { id ->
                User("zhangsan", 1000)
            }


}