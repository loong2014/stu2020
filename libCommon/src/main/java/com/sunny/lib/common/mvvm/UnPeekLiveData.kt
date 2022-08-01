package com.sunny.lib.common.mvvm

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *
 */
class UnPeekLiveData<T>(allowNull: Boolean = false) : ProtectedUnPeekLiveData<T>(allowNull) {

    override fun setValue(value: T) {
        super.setValue(value)
    }

    override fun postValue(value: T) {
        super.postValue(value)
    }


    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        throw IllegalAccessException("UnPeekLiveData can not execute observe!")
    }

    override fun observeForever(observer: Observer<in T>) {
        throw IllegalAccessException("UnPeekLiveData can not execute observeForever!")
    }

}

open class ProtectedUnPeekLiveData<T>(private val allowNullValue: Boolean = false) :
    MutableLiveData<T>() {

    private val changedMap = hashMapOf<Int, Boolean>()

    fun observeInActivity(activity: AppCompatActivity, observer: Observer<in T>) {
        val key = System.identityHashCode(activity.viewModelStore)
        observeIn(key, activity, observer)
    }

    fun observeInFragment(fragment: Fragment, observer: Observer<in T>) {
        val key = System.identityHashCode(fragment.viewModelStore)
        observeIn(key, fragment.viewLifecycleOwner, observer)
    }

    private fun observeIn(key: Int, owner: LifecycleOwner, observer: Observer<in T>) {
        if (changedMap[key] == null) {
            changedMap[key] = false
        }
        super.observe(owner) { t ->
            val changed = changedMap[key]
            if (changed == null || !changed) return@observe
            if (t != null || allowNullValue) {
                changedMap[key] = false
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(value: T) {
        if (value != null || allowNullValue) {
            for (item in changedMap.entries) {
                item.setValue(true)
            }
            super.setValue(value)
        }
    }

    protected fun clear() {
        super.setValue(null)
    }

}

fun <T> MutableLiveData<T>.safeSet(value: T?) {
    if (isMainThread()) {
        setValue(value)
    } else {
        LiveDataUtils.postSetValue(this, value)
    }
}


