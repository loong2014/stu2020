package com.sunny.module.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.sunny.lib.common.base.BaseActivity
import com.sunny.module.view.databinding.ViewActCommonBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LayoutCommonActivity : BaseActivity() {

    private lateinit var mActBinding: ViewActCommonBinding

    /*
    // Create a ViewModel the first time the system calls an activity's onCreate() method.
    // Re-created activities receive the same MyViewModel instance created by the first activity.

    // Use the 'by viewModels()' Kotlin property delegate
    // from the activity-ktx artifact
     */
    private val mViewModel: LayoutCommonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(object : LifecycleObserver{

        })

        mActBinding = DataBindingUtil.setContentView(this, R.layout.view_act_common)
//        final UserModel viewModel = new ViewModelProvider(this).get(UserModel.class);

        mActBinding.viewModel = mViewModel
        mActBinding.lifecycleOwner = this

        lifecycleScope.launch {
            val result = withContext(Dispatchers.Default) {
                delay(5_000)
                "hello lifecycle"
            }
            mActBinding.tabName.text = result

        }

        lifecycleScope.launch {
            whenResumed {

            }
        }
        initViewModel()
    }

    private fun initViewModel() {
        mViewModel.getTabList().observe(this) {
            mActBinding.tabName.text = mViewModel.getTabNames()
        }
        mViewModel.tipStr.observe(this) {
            mActBinding.tabName.text = mViewModel.getTabNames()
        }
    }


//
//    private Fragment newFragment() {
//        try {
//            Method get = fragmentClass.getMethod("newInstance");
//            return (Fragment) (get.invoke(fragmentClass));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        assert false : "Failed to create instance of " + fragmentClass.getName();
//        return null;
//    }
}