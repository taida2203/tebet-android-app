package com.tebet.mojual.view.bankconfirm

import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.view.base.BaseActivityNavigator

interface BankConfirmNavigator : BaseActivityNavigator {
    fun openOrderDetailScreen(order: OrderDetail)
    fun openBankUpdateScreen()
}
