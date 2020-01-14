package com.sunny.family.photoalbum

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.sunny.family.R
import com.sunny.family.photoalbum.fragment.FindFragment
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.ToastUtils
import kotlinx.android.synthetic.main.act_photo_album.*

/**
 * Created by zhangxin17 on 2020-01-10
 */
class PhotoAlbumActivity : BaseActivity() {


//    private FindFragment findFragment;
//    private HotFragment hotFragment;
//    private DailyFragment dailyFragment;

    private var hotFragment: FindFragment? = null
    private var findFragment: FindFragment? = null
    private var dailyFragment: FindFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_photo_album)

        initView()
        setListener()
    }

    private fun initView() {
        setSupportActionBar(main_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) //设置不显示标题
        main_toolbar.setNavigationIcon(R.drawable.ic_action_menu)

        //设置默认第一个菜单按钮为选中状态
        setChoice(1)
    }

    private fun setListener() {
        main_toolbar.setNavigationOnClickListener {
            ToastUtils.show("点击菜单按钮")
        }

        tv_daily.setOnClickListener {
            setChoice(1)
        }
        tv_find.setOnClickListener {
            setChoice(2)
        }
        tv_hot.setOnClickListener {
            setChoice(3)
        }
    }

    private fun setChoice(currentItem: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        clearChoice()

        when (currentItem) {
            // 每日精选
            1 -> {
                tv_daily.setTextColor(resources.getColor(R.color.colorBlack))
                if (dailyFragment == null) {
                    dailyFragment = FindFragment("每日精选")
                    transaction.add(R.id.main_ll_fragment, dailyFragment!!)
                } else {
                    transaction.show(dailyFragment!!)
                }
            }
            // 发现更多
            2 -> {
                tv_find.setTextColor(resources.getColor(R.color.colorBlack))

                if (findFragment == null) {
                    findFragment = FindFragment("发现更多")
                    transaction.add(R.id.main_ll_fragment, findFragment!!)
                } else {
                    transaction.show(findFragment!!)
                }
            }
            // 热门排行
            3 -> {
                tv_hot.setTextColor(resources.getColor(R.color.colorBlack))
                if (hotFragment == null) {
                    hotFragment = FindFragment("热门排行")
                    transaction.add(R.id.main_ll_fragment, hotFragment!!)
                } else {
                    transaction.show(hotFragment!!)
                }
            }
        }
        //提交事务
        transaction.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction) {
        dailyFragment?.let {
            transaction.hide(it)
        }

        findFragment?.let {
            transaction.hide(it)
        }

        hotFragment?.let {
            transaction.hide(it)
        }
    }


    /**
     * 重置所有选项
     */
    private fun clearChoice() { //还原默认选项
        tv_daily.setTextColor(resources.getColor(R.color.colorGray))
        tv_find.setTextColor(resources.getColor(R.color.colorGray))
        tv_hot.setTextColor(resources.getColor(R.color.colorGray))
    }

}