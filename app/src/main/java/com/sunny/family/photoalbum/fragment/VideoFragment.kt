package com.sunny.family.photoalbum.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunny.family.R
import com.sunny.family.adapter.CommonAdapter
import com.sunny.family.adapter.ViewHolder
import com.sunny.family.model.VideoModel
import com.sunny.family.photoalbum.PhotoAlbumHelper
import com.sunny.lib.base.BaseFragment
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.jump.params.JumpPlayerParam
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.fragment_find.*

class VideoFragment(private val tip: String) : BaseFragment() {

    private val logTag = "VideoFragment"

    private lateinit var rootView: View
    private lateinit var photoAlbumHelper: PhotoAlbumHelper

    private var dataList: List<VideoModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_find, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tip_name.text = tip

        photoAlbumHelper = PhotoAlbumHelper()

        initData()

        setListener()
    }

    private fun initData() {
        photoAlbumHelper.getVideoList(object : PhotoAlbumHelper.PhotoAlbumCallback {

            override fun onGetVideoList(list: List<VideoModel>) {
                SunLog.i(logTag, "onGetVideoList  ${list.size}")

                setAdapter(list)
            }
        })
    }

    //设置适配器
    private fun setAdapter(list: List<VideoModel>) {
        dataList = list

        if (dataList.isNullOrEmpty()) {
            return
        }

        val context = activity ?: ContextProvider.appContext

        find_grid.adapter = object : CommonAdapter<VideoModel>(
                context = context, dataList = dataList, viewLayoutId = R.layout.item_view_video) {

            override fun convert(viewHolder: ViewHolder, data: VideoModel) {
                viewHolder.setImage(R.id.item_img, data.path)

                viewHolder.setText(R.id.item_name, data.name)
                viewHolder.setText(R.id.item_duration, data.duration.toString())
            }
        }
    }

    private fun setListener() {
        find_grid.setOnItemClickListener { parent, view, position, id ->
            tryJumpPlayerPage(dataList?.get(position))
        }
    }

    private fun tryJumpPlayerPage(data: VideoModel?) {
        if (data == null) {
            SunLog.i(logTag, "tryJumpPlayerPage  data is null")
            return
        }

        val jumpParams = JumpPlayerParam()
        jumpParams.videoPath = data.path
        jumpParams.videoName = data.name

        PageJumpUtils.jumpPlayerPage(context = activity, jumpParams = jumpParams)

    }

}