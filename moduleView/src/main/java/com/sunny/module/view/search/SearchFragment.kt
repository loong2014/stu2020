package com.sunny.module.view.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.sunny.lib.common.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val mViewModel: SearchViewModel by viewModels()

    //    lateinit var mViewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

//        mViewModel = ViewModelProvider.AndroidViewModelFactory(activity.application).create(SearchViewModel::class.java)
//        mViewModel =  ViewModelProviders.of(this, viewModelFactory).get(MovieViewModel.class);


        lifecycle.coroutineScope
        lifecycleScope.launch {
            val result = withContext(Dispatchers.Default) {
                delay(5_000)

                "hello lifecycle"
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initViewModel() {

    }
}