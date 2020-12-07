package com.sunny.family.provider

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.family.R
import com.sunny.lib.base.BaseActivity
import com.sunny.lib.router.RouterConstant
import kotlinx.android.synthetic.main.act_provider.*

/**
 * Created by zhangxin17 on 2020/12/7
 */
@Route(path = RouterConstant.PageProvider)
class ProviderActivity : BaseActivity() {

    private val contactsList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_provider)

        initView()

        btn_read_contacts.setOnClickListener {
            doReadContacts()
        }
    }

    private fun initView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
        list_view.adapter = adapter

    }

    private fun doReadContacts() {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null)?.apply {
            while (moveToNext()) {
                val displayName = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contactsList.add("$displayName \n\t $number")
            }
            adapter.notifyDataSetChanged()
            close()
        }
    }

}