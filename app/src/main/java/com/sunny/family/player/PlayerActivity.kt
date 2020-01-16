package com.sunny.family.player

import android.os.Bundle
import com.google.gson.Gson
import com.sunny.family.R
import com.sunny.lib.jump.JumpConfig
import com.sunny.lib.jump.params.JumpPlayerParams
import com.sunny.player.BasePlayerActivity

class PlayerActivity : BasePlayerActivity() {

    var jumpParams: JumpPlayerParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(JumpConfig.keyJumpParams)?.let {
            jumpParams = Gson().fromJson(it, JumpPlayerParams::class.java)
        }

        setContentView(R.layout.act_player)

    }


}