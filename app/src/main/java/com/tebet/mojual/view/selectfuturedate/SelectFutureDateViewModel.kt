package com.tebet.mojual.view.selectfuturedate

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SelectFutureDateViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<SelectFutureDateNavigator>(dataManager, schedulerProvider) {
    var items: ObservableList<String> = ObservableArrayList()
    var itemBinding: ItemBinding<String> =
        ItemBinding.of<String>(BR.item, R.layout.item_select_future_date)
            .bindExtra(BR.listener, object : OnFutureDateClick {
                override fun onItemClick(item: String) {
                    navigator.itemSelected(item)
                }
            })

    fun loadData() {
        val quantity = arrayListOf(1, 2, 3, 4, 5, 6, 7)
        quantity.forEach { item ->
            items.add(item.toString())
        }
    }

    interface OnFutureDateClick {
        fun onItemClick(item: String)
    }
}