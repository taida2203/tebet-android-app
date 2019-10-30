package com.tebet.mojual.view.myasset

import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.view.base.BaseActivityNavigator

interface MyAssetNavigator :BaseActivityNavigator{
    fun itemSelected(item: Asset)
}
