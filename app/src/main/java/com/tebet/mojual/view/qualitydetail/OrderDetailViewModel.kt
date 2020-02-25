package com.tebet.mojual.view.qualitydetail

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.OnListItemClick
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.*
import com.tebet.mojual.data.models.enumeration.AssetAction
import com.tebet.mojual.data.models.enumeration.ContainerOrderStatus
import com.tebet.mojual.data.models.enumeration.DocumentType
import com.tebet.mojual.data.models.request.CreateOrderDocumentRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit


class OrderDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<OrderDetailNavigator>(dataManager, schedulerProvider) {
    var awardExpaned: ObservableBoolean = ObservableBoolean(false)
    var totalExpaned: ObservableBoolean = ObservableBoolean(false)
    var totalPrice: ObservableDouble = ObservableDouble()
    var totalPriceToPay: ObservableDouble = ObservableDouble()
    var totalBonus: ObservableDouble = ObservableDouble()
    var totalReleaseBonus: ObservableDouble = ObservableDouble()
    var totalDelivery: ObservableDouble = ObservableDouble()
    var totalAward: ObservableDouble = ObservableDouble()
    var order: ObservableField<OrderDetail> = ObservableField()
    var items: ObservableArrayList<OrderContainer> = ObservableArrayList()
    var itemBinding: ItemBinding<OrderContainer> =
        ItemBinding.of<OrderContainer>(BR.item, R.layout.item_order_detail)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemExpandClick(item: OrderContainer) {
                    item.expanded = !item.expanded
                }

                override fun onItemClick(item: OrderContainer) {
                    item.isSelected = !item.isSelected
                    updateTotalPrice()
                    navigator.itemSelected(item)
                }
            })
    var documents: ObservableArrayList<OrderDocument> = ObservableArrayList()
    var documentsBinding: ItemBinding<OrderDocument> =
        ItemBinding.of<OrderDocument>(BR.item, R.layout.item_order_detail_document)
            .bindExtra(BR.listener,
                object : OnListItemClick<OrderDocument> {
                    override fun onItemClick(item: OrderDocument) {
                        navigator.documentSelected(item)
                    }
                })
            .bindExtra(BR.listenerDelete,
                object : OnListItemClick<OrderDocument> {
                    override fun onItemClick(item: OrderDocument) {
                        navigator.documentDeleteConfirm(item)
                    }
                })
    var selectedDocument: CreateOrderDocumentRequest? = null

    private fun updateTotalPrice() {
        when {
            order.get()?.canAction == true -> {
                totalPrice.set(items.filter { it.isSelected }.sumByDouble {
                    it.priceTotalDisplay ?: 0.0})
                totalReleaseBonus.set(if (items.firstOrNull { it.isSelected } != null) order.get()?.releaseBonus
                    ?: 0.0 else 0.0)
                totalPriceToPay.set(totalPrice.get() + totalReleaseBonus.get())
                totalBonus.set(items.filter { it.isSelected }.sumByDouble {
                    it.totalVolumeBonus ?: 0.0
                })
                totalDelivery.set(if (items.firstOrNull { it.isSelected } != null) order.get()?.deliveryBonus
                    ?: 0.0 else 0.0)
                totalAward.set(if (items.firstOrNull { it.isSelected } != null) (totalBonus.get() + totalDelivery.get()) else 0.0)
            }
            else -> {
                totalPrice.set(order.get()?.totalPrice ?: 0.0)
                order.get()?.releaseBonus?.let { totalReleaseBonus.set(it) }
                totalPriceToPay.set(order.get()?.amountToPay ?: 0.0)
                order.get()?.bonus?.let { totalBonus.set(it) }
                order.get()?.deliveryBonus?.let { totalDelivery.set(it) }
                totalAward.set(totalDelivery.get() + totalBonus.get())
            }
        }
    }

    override fun loadData(isForceLoad: Boolean?) {
        order.get()?.let {
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.getOrderDetail(it.orderId, loadContainers = true, loadCustomer = true, loadDocuments = true)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<OrderDetail>() {
                        override fun onSuccess(dataResponse: OrderDetail) {
                            order.set(dataResponse)
                            dataResponse.containers?.forEach { container ->
                                if(container.isRejected) container.isSelected = false
                                if (!items.contains(container)) items.add(container)
                                else items[items.indexOf(container)] = container
                            }
                            documents.clear()
                            dataResponse.orderDocuments?.sortedByDescending { doc -> doc.orderDocumentId }
                                ?.let { it1 -> documents.addAll(it1) }
                            updateTotalPrice()
                            navigator.showLoading(false)
                        }

                        override fun onFailure(error: NetworkError) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }

    interface OnFutureDateClick {
        fun onItemClick(item: OrderContainer)
        fun onItemExpandClick(item: OrderContainer)
    }

    fun onRejectClick() {
        navigator.showRejectConfirm()
    }

    fun onAwardClick() {
        awardExpaned.set(!awardExpaned.get())
    }

    fun onTotalClick() {
        if (totalReleaseBonus.get() <= 0) return
        totalExpaned.set(!totalExpaned.get())
    }

    fun rejectOrder() {
        val rejectItems = items.toList().map {
            it.action = AssetAction.REJECT.name
            it
        }
        if (rejectItems.firstOrNull { it.status?.equals(ContainerOrderStatus.FIRST_FINALIZED_PRICE_OFFERED.name) == true } != null) {
            order.get()?.let { navigator.openReasonScreen(it, rejectItems) }
        } else {
            submitOrder(rejectItems)
        }
    }

    fun onSubmitClick() {
        if (!navigator.validate()) {
            return
        }
        if (items.firstOrNull { item -> item.isSelected } == null) {
            navigator.show(R.string.quality_check_error_select_order)
            return
        }
        navigator.showConfirmDialog(items)
    }

    fun onUploadDocumentClick() {
        selectedDocument = CreateOrderDocumentRequest("", DocumentType.OTHER.name, "")
        navigator.uploadDocument()
    }

    private fun submitOrder(selectedItems: List<OrderContainer>) {
        order.get()?.let { order ->
            navigator.showLoading(true)
            compositeDisposable.add(
                dataManager.confirmOrder(order.orderId, selectedItems)
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Order>() {
                        override fun onSuccess(dataResponse: Order) {
                            navigator.showLoading(false)
                            this@OrderDetailViewModel.order.get()?.let {
                                navigator.openOrderDetailScreen(it)
                            }
                        }

                        override fun onFailure(error: NetworkError) {
                            navigator.showLoading(false)
                            handleError(error)
                        }
                    })
            )
        }
    }

    fun approveOrder(selectedItems: List<OrderContainer>) {
        if (selectedItems.firstOrNull {
                it.status?.equals(ContainerOrderStatus.FIRST_FINALIZED_PRICE_OFFERED.name) == true ||
                        it.status?.equals(ContainerOrderStatus.SECOND_FINALIZED_PRICE_OFFERED.name) == true
            } != null) {
            order.get()?.let { order ->
                navigator.openBankConfirmScreen(order, selectedItems)
            }
        } else {
            submitOrder(selectedItems)
        }
    }

    fun uploadDocument(currentPhotoPath: String) {
        navigator.showLoading(true)
        compositeDisposable.add(uploadImage(currentPhotoPath, "document")
            .concatMap {
                selectedDocument?.filePath = it.data!!
                order.get()?.orderId?.let { it1 -> dataManager.createOrderDocument(it1, listOf(selectedDocument!!)) }
            }
            .observeOn(schedulerProvider.ui())
            .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                override fun onSuccess(dataResponse: EmptyResponse) {
                    navigator.showLoading(false)
                    loadData(true)
                }

                override fun onFailure(error: NetworkError) {
                    navigator.showLoading(false)
                    if (error.errorCode == 500) {
                        loadData(true)
                        return
                    }
                    handleError(error)
                }
            })
        )
    }

    private fun uploadImage(imagePath: String?, folderName: String): Observable<AuthJson<String>> {
        val uploadText = MultipartBody.Part.createFormData("folder", folderName)
        val file = File(imagePath)
        val uploadData = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/png"), file)
        )
        return dataManager.uploadImage(uploadText, uploadData).subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .debounce(400, TimeUnit.MILLISECONDS)
    }

    fun deleteOrder(item: OrderDocument) {
        navigator.showLoading(true)
        compositeDisposable.add(dataManager.deleteOrderDocument(listOf(item.orderDocumentId))
            .observeOn(schedulerProvider.ui())
            .subscribeWith(object : CallbackWrapper<EmptyResponse>() {
                override fun onSuccess(dataResponse: EmptyResponse) {
                    navigator.showLoading(false)
                    loadData(true)
                }

                override fun onFailure(error: NetworkError) {
                    navigator.showLoading(false)
                    if (error.errorCode == 500) {
                        loadData(true)
                        return
                    }
                    handleError(error)
                }
            })
        )
    }
}