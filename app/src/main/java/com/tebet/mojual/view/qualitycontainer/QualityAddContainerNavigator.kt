package com.tebet.mojual.view.qualitycontainer

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.view.base.BaseActivityNavigator
import io.reactivex.Observable

interface QualityAddContainerNavigator: BaseActivityNavigator {
    fun openConfirmScreen(dataResponse: Order)
    fun dataValid(): Boolean
    fun requestLocationAndConnectIOT()
    fun showTurnOffIOTDialog()
    fun openQualityHelpScreen()
}
