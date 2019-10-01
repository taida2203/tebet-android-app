package com.tebet.mojual.data.models.request

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.data.models.enumeration.OrderStatus
import com.tebet.mojual.data.models.enumeration.SortBy
import com.tebet.mojual.data.models.enumeration.SortType

data class SearchOrderRequest(
    var quantity: Int? = null,
    var planDate: Long? = null,

    var fromAmount: Double? = null,
    var toAmount: Double? = null,

    var fromSaleDate: Long? = null,
    var toSaleDate: Long? = null,

    var status: String? = null,
    var statusList: String? = null,

    var containerStatus: String? = null,
    var containerStatusList: String? = null,

    var loadCustomer: Boolean? = null,
    var loadContainers: Boolean? = null,
    var hasNoContainer: Boolean? = null,
    var profileId: Int? = null,
    var profileCode: String? = null,
    var orderCode: String? = null,
    var orders: HashMap<String, String>? = hashMapOf(Pair(SortBy.ORDER_ID.value, SortType.DESC.name)),
    var limit: Int? = null,
    var offset: Int? = null
) : BaseObservable() {
    var fromPlanDate: Long? = null
    @Bindable get
    set(value) {
        field = value
        notifyPropertyChanged(BR.fromPlanDate)
    }
    var toPlanDate: Long? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.toPlanDate)
        }

    var selectedStatus: OrderStatus? = null
        @Bindable get
        set(value) {
            field = value
            status = if (value != OrderStatus.ALL) value?.name else null
            notifyPropertyChanged(BR.selectedStatus)
        }

    var selectedSortBy: SortBy? = SortBy.SALE_DATE
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedSortBy)
        }

    var selectedSortType: SortType = SortType.DESC
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedSortType)
        }
}


//
//var Map<String, String> orders,
