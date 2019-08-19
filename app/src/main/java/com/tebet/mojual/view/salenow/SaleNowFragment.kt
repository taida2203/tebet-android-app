package com.tebet.mojual.view.salenow

import com.tebet.mojual.data.models.Order
import com.tebet.mojual.view.home.Home
import com.tebet.mojual.view.sale.SaleFragment

class SaleNowFragment : SaleFragment() {
    override fun openSaleScreen(dataResponse: Order) {
        (activity as Home).viewModel.onSubmitOrderNowClick(dataResponse)
    }
}
