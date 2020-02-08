package com.sunny.family.photoalbum.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunny.family.R
import com.sunny.family.adapter.CommonAdapter
import com.sunny.family.adapter.ViewHolder
import com.sunny.family.model.PictureModel
import com.sunny.family.photoalbum.PhotoAlbumHelper
import com.sunny.lib.base.BaseFragment
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.fragment_picture.*

class PictureFragment(private val tip: String) : BaseFragment() {

    private val logTag = "PictureFragment"

    private lateinit var rootView: View
    private lateinit var photoAlbumHelper: PhotoAlbumHelper

    private var dataList: List<PictureModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_picture, container, false)
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
        photoAlbumHelper.getPictureList(object : PhotoAlbumHelper.PhotoAlbumCallback {

            override fun onGetPictureList(list: List<PictureModel>) {
                SunLog.i(logTag, "onGetPictureList  ${list.size}")

                setAdapter(list)
            }
        })
    }

    //设置适配器
    private fun setAdapter(list: List<PictureModel>) {
        dataList = list

        if (dataList.isNullOrEmpty()) {
            return
        }

        val context = activity ?: ContextProvider.appContext

        find_grid.adapter = object : CommonAdapter<PictureModel>(
                context = context, dataList = dataList, viewLayoutId = R.layout.item_view_picture) {

            override fun convert(viewHolder: ViewHolder, data: PictureModel) {
                viewHolder.setText(R.id.item_name, data.name)

                viewHolder.setImage(R.id.item_img, data.path)
            }
        }
    }

    private fun setListener() {
        find_grid.setOnItemClickListener { parent, view, position, id ->
            //            tryJumpPlayerPage(pictureList?.get(position))
        }
    }
}