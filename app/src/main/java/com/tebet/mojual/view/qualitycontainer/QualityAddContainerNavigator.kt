package com.tebet.mojual.view.qualitycontainer

import com.tebet.mojual.view.base.BaseActivityNavigator

interface QualityAddContainerNavigator: BaseActivityNavigator {
    fun openHomeScreen()
    fun dataValid(): Boolean
}
