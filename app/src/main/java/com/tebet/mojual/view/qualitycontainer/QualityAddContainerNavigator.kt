package com.tebet.mojual.view.qualitycontainer

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.view.base.BaseActivityNavigator

interface QualityAddContainerNavigator: BaseActivityNavigator {
    fun openConfirmScreen(dataResponse: Order)
    fun dataValid(): Boolean
}
