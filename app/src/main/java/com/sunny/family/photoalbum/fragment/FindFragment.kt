package com.sunny.family.photoalbum.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunny.family.R
import com.sunny.family.model.MediaModel
import com.sunny.family.photoalbum.PhotoAlbumHelper
import com.sunny.lib.base.BaseFragment
import com.sunny.lib.jump.JumpConfig
import com.sunny.lib.jump.PageJumpUtils
import com.sunny.lib.jump.params.JumpPlayerParams
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.fragment_find.*

class FindFragment(private val tip: String) : BaseFragment() {

    private val logTag = "FindFragment"

    private lateinit var rootView: View

    private lateinit var photoAlbumHelper: PhotoAlbumHelper

    private var pictureList: List<MediaModel>? = null

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
        photoAlbumHelper.getPictureList(photoAlbumCallback)

    }

    private val photoAlbumCallback: PhotoAlbumHelper.PhotoAlbumCallback = object : PhotoAlbumHelper.PhotoAlbumCallback {
        override fun onGetPicturePathList(list: List<MediaModel>) {
            SunLog.i(logTag, "onGetPicturePathList  ${list.size}")
            pictureList = list
        }
    }

    private fun setListener() {

        find_grid.setOnItemClickListener { parent, view, position, id ->
            tryJumpPlayerPage(pictureList?.get(position))
        }
    }

    private fun tryJumpPlayerPage(data: MediaModel?) {
        SunLog.i(logTag, "tryJumpPlayerPage  $data")
        data?.let {
            val params = JumpPlayerParams()
            params.videoName = it.name
            params.videoPath = it.path

            val intent = Intent()
            intent.putExtra(JumpConfig.keyJumpParams, params.toString())
            PageJumpUtils.jumpPlayerPage(intent, activity)
        }
    }

}