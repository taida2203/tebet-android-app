package com.tebet.mojual.view.sale

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.view.base.BaseActivityNavigator

interface SaleNavigator : BaseActivityNavigator {
    fun openSaleScreen(dataResponse: Order)
    fun showQuantityScreen()
    fun showDateScreen()
    fun validate(): Boolean
}
