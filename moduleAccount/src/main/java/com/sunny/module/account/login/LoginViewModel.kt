package com.sunny.module.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunny.module.account.LoginUtils
import com.sunny.module.account.UserModel


/**
 * Created by zhangxin17 on 2021/1/25
 */
class LoginViewModel(lastLoginName: String = "游客", count: Int = 0) : ViewModel() {

    /**
     * MutableLiveData 是一种可变的LiveData
     * setValue(value)
     * getValue():value
     * postValue(value)     : 用户在非主线程中给LiveData设置数据
     */

    // 使用loginCount对_loginCount进行包装，只暴露获取接口，从而包装数据的封装性
    val loginCount: LiveData<Int>
        get() = _loginCount

    private val _loginCount = MutableLiveData<Int>()

    private val _userModel = MutableLiveData<UserModel>()

    /**
     * 通过map将实际包含数据的LiveData和仅用于观察数据的LiveData进行转换
     * 这里仅暴露UserModel中的name
     */
    val userName: LiveData<String> = Transformations.map(_userModel) {
        it.name
    }


    /**
     * 使用switchMap解决liveData实例一直变化的情况
     */
    private val _userIdLiveData = MutableLiveData<Long>()
    val user: LiveData<UserModel> = Transformations.switchMap(_userIdLiveData) { userId ->
        getUserModelLiveData(userId)
    }

    /**
     * 1. 更改_userIdLiveData的值
     * 2. 观察_userIdLiveData的switchMap方法被执行，
     * 在switchMap方法中，获取真正的用户数据，同时将返回的LiveData转换成一个可观察的LiveData对象
     * 3. 通过user观察数据变化的observer将被触发执行
     *
     * 当model中没有userId来进行观察时，可以创建一个空的LiveData对象
     */
    private val _emptyLiveData = MutableLiveData<Any>()
    val emptyUser: LiveData<UserModel> = Transformations.switchMap(_emptyLiveData) { userId ->
        getUserModelLiveData(0)
    }

    fun updateUserId() {
        _userIdLiveData.value = LoginUtils.getUserInfo()?.id
    }

    fun updateEmptyAny() {
        _emptyLiveData.value = _emptyLiveData.value
    }

    private fun getUserModelLiveData(userId: Long): LiveData<UserModel> {
        val liveData = MutableLiveData<UserModel>()
        liveData.value = LoginUtils.getUserInfo() ?: UserModel(name = "test")
        return liveData
    }

    init {
        _loginCount.value = -1
        _userModel.value = UserModel(name = "zhang")
    }

    var loginName: String = lastLoginName // 用户名

    fun doLogin() {
        val count = loginCount.value ?: 0
        _loginCount.value = count + 1
    }

    fun doReset() {
        _loginCount.value = 0
    }

}