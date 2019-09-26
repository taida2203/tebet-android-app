package com.tebet.mojual.view.message

import androidx.databinding.ObservableArrayList
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.OnListItemClick
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Message
import com.tebet.mojual.data.models.Paging
import com.tebet.mojual.data.models.request.MessageRequest
import com.tebet.mojual.data.remote.CallbackWrapper
import com.tebet.mojual.view.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass


class MessageViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<MessageNavigator>(dataManager, schedulerProvider) {
    var items: ObservableArrayList<Message> = ObservableArrayList()

    /**
     * Items merged with a header on top
     */
    var headerFooterItems: MergeObservableList<Any> = MergeObservableList<Any>()
        .insertList(items)

    val multipleItemsBind: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(Message::class.java) { itemBinding, position, item ->
            itemBinding.set(BR.item, R.layout.item_message)
            itemBinding.bindExtra(BR.listener, object : OnListItemClick<Message> {
                override fun onItemClick(item: Message) {
                    navigator.openNotificationDetail(item)
                }
            })
        }

    override fun loadData(isForceLoad: Boolean?) {
        super.loadData(isForceLoad)
        if (isForceLoad == true) {
            items.clear()
        }
        loadData(0)
    }
    fun loadData(page: Int = 0) {
        val offset = page * 10
        if (items.size >= offset) {
            navigator.showLoading(true)
//        headerFooterItems.insertItem(R.layout.item_quality_loading)
            compositeDisposable.add(
                dataManager.getUserProfileDB()
                    .concatMap {
                        it.data?.profileId?.let { profileId ->
                            dataManager.getMessages(MessageRequest(profileId = profileId, offset = (page) * 10, limit = 10)) }
                    }
                    .observeOn(schedulerProvider.ui())
                    .subscribeWith(object : CallbackWrapper<Paging<Message>>() {
                        override fun onSuccess(dataResponse: Paging<Message>) {
                            dataResponse.data.map { order ->
                                if (order.data == null) {
                                    order.data = emptyMap()
                                }
                                order
                            }.forEach { order ->
                                if (!items.contains(order)) items.add(order) else items[items.indexOf(order)] = order
                            }

                            navigator.showLoading(false)
//                        headerFooterItems.removeItem(R.layout.item_quality_loading)
                        }

                        override fun onFailure(error: String?) {
                            navigator.showLoading(false)
//                        headerFooterItems.removeItem(R.layout.item_quality_loading)
                            handleError(error)
                        }
                    })
            )
        }
    }
}