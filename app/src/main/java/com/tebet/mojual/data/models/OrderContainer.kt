package com.tebet.mojual.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Ignore
import com.tebet.mojual.data.models.enumeration.AssetAction
import com.tebet.mojual.data.models.enumeration.ContainerOrderState
import com.tebet.mojual.data.models.enumeration.ContainerOrderStatus
import java.io.Serializable

data class OrderContainer(
    var assetId: Long? = null,
    var assetCode: String? = null,
    var orderId: Long? = null,
    var orderCode: String? = null,
    var combinedCode: String? = null,
    var basePrice: Double? = null,
    var initialPrice: Double? = null,
    var firstFinalizedPrice: Double? = null,
    var secondFinalizedPrice: Double? = null,
    var initialPriceTotal: Double? = null,
    var firstFinalizedPriceTotal: Double? = null,
    var secondFinalizedPriceTotal: Double? = null,
    var holderProfileId: Long? = null,
    var densityQc: Double? = null,
    var temperatureQc: Double? = null,
    var eugenolQc: Double? = null,
    var weightCs: Double? = null,
    var weightLs: Double? = null,
    var densityIot: Double? = null,
    var temperatureIot: Double? = null,
    var eugenolIot: Double? = null,
    var pricePerKg: Double? = null,
    var status: String? = null,
    var createdBy: String? = null,
    var createdDate: Long? = null,
    var modifiedBy: String? = null,
    var modifiedDate: Long? = null,
    var totalVolumeBonus: Double? = null,
    var action: String? = AssetAction.APPROVE.name,
    var orderAnswers : List<Question> = arrayListOf()
) : BaseObservable(), Serializable {
    val state: ContainerOrderState
        get() {
            return when (status) {
                // Initial price offerred
                ContainerOrderStatus.INITIAL_PRICE_OFFERED.name -> ContainerOrderState.INITIAL_PRICE_OFFERED // container pass first verification by IOT device, and container is assigned initial price
                // Quality rejected
                ContainerOrderStatus.QUALITY_REJECTED.name -> ContainerOrderState.QUALITY_REJECTED // container NOT pass first verification by IOT device
                // Initial price rejected
                ContainerOrderStatus.INITIAL_PRICE_REJECTED.name -> ContainerOrderState.INITIAL_PRICE_REJECTED // Customer not accept initial price and don’t want to sale
                // Sale requested
                ContainerOrderStatus.SALE_REQUESTED.name,
                ContainerOrderStatus.CONTACTED_LOGISTIC_TO_PICK.name -> ContainerOrderState.SALE_REQUESTED // Customer want to sale and submitted sale request
                // Checking
                ContainerOrderStatus.PICKED_TO_FACTORY.name, // container is picked up to Factory (on the way to the factory
                ContainerOrderStatus.ARRIVED_IN_FACTORY.name -> ContainerOrderState.CHECKING // container is arrived in Factory
                // Sale rejected
                ContainerOrderStatus.SALE_REJECTED.name, // Customer reject final price offering (don’t want to sale)
                ContainerOrderStatus.WILL_BE_RETURNED.name, // container will be returned back to customer
                ContainerOrderStatus.CONTACTED_LOGISTIC_TO_RETURN.name, // Logistic staff already contacted Logistic company to return
                ContainerOrderStatus.PICKED_TO_RETURN.name -> ContainerOrderState.SALE_REJECTED // container is picked by Logistic Staff to return back to customer
                // Returned
                ContainerOrderStatus.RETURNED_TO_CUSTOMER.name -> ContainerOrderState.RETURNED // container is returned to customer
                // Final price offerred
                ContainerOrderStatus.FIRST_FINALIZED_PRICE_OFFERED.name, // System automatic calculate final result checking and generate first Final Price
                ContainerOrderStatus.SECOND_FINALIZED_PRICE_OFFERED.name -> ContainerOrderState.FINAL_PRICE_OFFERED // Admin offer second finalized price to customer
                // Sale confirmed
                ContainerOrderStatus.SALE_CONFIRMED.name, // Customer confirm want to Sale with final price
                ContainerOrderStatus.PAYMENT_IN_PROGRESS.name, // system processing payment
                ContainerOrderStatus.PAYMENT_FAILURE.name -> ContainerOrderState.SALE_CONFIRMED // payment is failure because any problem
                // Paid
                ContainerOrderStatus.PAID_TO_CUSTOMER.name -> ContainerOrderState.PAID// payment successfully to customer
                else -> ContainerOrderState.UNKNOWN
            }
        }

    val pricePerKgDisplay: Double?
        get() {
            if (secondFinalizedPrice != null) {
                return secondFinalizedPrice
            }
            if (firstFinalizedPrice != null) {
                return firstFinalizedPrice
            }
            if (initialPrice != null) {
                return initialPrice
            }
            return null
        }

    val weightDisplay: Double?
        get() {
            if (weightLs != null) {
                return weightLs
            }
            if (weightCs != null) {
                return weightCs
            }
            return null
        }

    val eugenolDisplay: Double?
        get() {
            eugenolQc?.let { return it * 100 }
            eugenolIot?.let { return it * 100 }
            return null
        }

    val priceTotalDisplay: Double?
        get() {
            if (secondFinalizedPriceTotal != null) {
                return secondFinalizedPriceTotal
            }
            if (firstFinalizedPriceTotal != null) {
                return firstFinalizedPriceTotal
            }
            if (initialPriceTotal != null) {
                return initialPriceTotal
            }
            return null
        }

    val canAction: Boolean
        get() {
            when (status) {
                ContainerOrderStatus.INITIAL_PRICE_OFFERED.name,
                ContainerOrderStatus.SECOND_FINALIZED_PRICE_OFFERED.name
                -> return true
            }
            return false
        }

    val isRejected: Boolean
        get() {
            return when (state) {
                ContainerOrderState.QUALITY_REJECTED,
                ContainerOrderState.INITIAL_PRICE_REJECTED,
                ContainerOrderState.SALE_REJECTED -> true
                else -> false
            }
        }

    var isSelected: Boolean = true
        @Bindable get
        set(value) {
            field = value
            action = if (value) AssetAction.APPROVE.name else AssetAction.REJECT.name
            notifyPropertyChanged(BR.selected)
        }

    @Ignore
    var expanded: Boolean = true
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.expanded)
        }

    override fun equals(other: Any?): Boolean {
        if (other is OrderContainer) {
            return assetId == other.assetId && orderId == other.orderId
        }
        return super.equals(other)
    }
}
