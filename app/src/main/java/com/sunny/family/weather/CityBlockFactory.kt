package com.sunny.family.weather

import android.view.ViewGroup
import com.sunny.family.weather.block.*
import com.sunny.view.block.ISunBlockFactory
import com.sunny.view.holder.SunBlockHolder

class CityBlockFactory : ISunBlockFactory {

    override fun createBlockHolder(parent: ViewGroup, uiType: Int): SunBlockHolder {
        return when (uiType) {
            CityBlockTipHolder.BlockType -> CityBlockTipHolder.create(parent)

            CityNormalBlockHolder.BlockType -> CityNormalBlockHolder.create(parent)

            CityDirectBlockHolder.BlockType -> CityDirectBlockHolder.create(parent)

            CityAutonomyBlockHolder.BlockType -> CityAutonomyBlockHolder.create(parent)

            CitySpecialBlockHolder.BlockType -> CitySpecialBlockHolder.create(parent)

            else -> CityBlockTipHolder.create(parent)
        }
    }
}