package com.tebet.mojual.view.qualitydetail

import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.OrderDocument
import com.tebet.mojual.view.base.BaseActivityNavigator

interface OrderDetailNavigator : BaseActivityNavigator {
    fun validate(): Boolean
    fun itemSelected(item: OrderContainer)
    fun openOrderDetailScreen(it: OrderDetail)
    fun showRejectConfirm()
    fun showConfirmDialog(selectedItems: List<OrderContainer>)
    fun openBankConfirmScreen(order: OrderDetail, selectedItems: List<OrderContainer>)
    fun openReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>)
    fun uploadDocument()
    fun documentSelected(item: OrderDocument)
    fun documentDeleteConfirm(item: OrderDocument)
}
