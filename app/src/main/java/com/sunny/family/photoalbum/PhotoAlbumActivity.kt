package com.sunny.family.photoalbum

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.sunny.family.R
import com.sunny.family.photoalbum.fragment.PictureFragment
import com.sunny.family.photoalbum.fragment.VideoFragment
import com.sunny.family.photoalbum.fragment.VoiceFragment
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.utils.SunToast
import kotlinx.android.synthetic.main.act_photo_album.*

/**
 * Created by zhangxin17 on 2020-01-10
 */
class PhotoAlbumActivity : BaseActivity() {

    private var firstFragment: PictureFragment? = null
    private var secondFragment: VideoFragment? = null
    private var thirdFragment: VoiceFragment? = null

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

        tv_1.text = "图片"
        tv_2.text = "视频"
        tv_3.text = "音频"

        //设置默认第一个菜单按钮为选中状态
        setChoice(1)
    }

    private fun setListener() {
        main_toolbar.setNavigationOnClickListener {
            SunToast.show("点击菜单按钮")
        }

        tv_1.setOnClickListener {
            setChoice(1)
        }
        tv_2.setOnClickListener {
            setChoice(2)
        }
        tv_3.setOnClickListener {
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
                tv_1.setTextColor(resources.getColor(R.color.colorBlack))
                if (firstFragment == null) {
                    firstFragment = PictureFragment("图片")
                    transaction.add(R.id.main_ll_fragment, firstFragment!!)
                } else {
                    transaction.show(firstFragment!!)
                }
            }
            // 发现更多
            2 -> {
                tv_2.setTextColor(resources.getColor(R.color.colorBlack))
                if (secondFragment == null) {
                    secondFragment = VideoFragment("视频")
                    transaction.add(R.id.main_ll_fragment, secondFragment!!)
                } else {
                    transaction.show(secondFragment!!)
                }
            }
            // 热门排行
            3 -> {
                tv_3.setTextColor(resources.getColor(R.color.colorBlack))
                if (thirdFragment == null) {
                    thirdFragment = VoiceFragment("热门排行")
                    transaction.add(R.id.main_ll_fragment, thirdFragment!!)
                } else {
                    transaction.show(thirdFragment!!)
                }
            }
        }
        //提交事务
        transaction.commit()
    }

    /**
     * 隐藏所有fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        firstFragment?.let {
            transaction.hide(it)
        }

        secondFragment?.let {
            transaction.hide(it)
        }

        thirdFragment?.let {
            transaction.hide(it)
        }
    }

    /**
     * 重置所有选项
     */
    private fun clearChoice() { //还原默认选项
        tv_1.setTextColor(resources.getColor(R.color.colorGray))
        tv_2.setTextColor(resources.getColor(R.color.colorGray))
        tv_3.setTextColor(resources.getColor(R.color.colorGray))
    }

}