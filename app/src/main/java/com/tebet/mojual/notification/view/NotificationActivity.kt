package com.tebet.mojual.notification.view


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.items
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.listeners.OnClickListener
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener
import com.tebet.mojual.common.base.BaseActivity
import com.tebet.mojual.R
import com.tebet.mojual.data.remote.NotificationNewResponse
import com.tebet.mojual.data.remote.NotificationRequest
import com.tebet.mojual.data.remote.NotificationsResponseV2
import com.tebet.mojual.notification.NotificationTitle
import com.tebet.mojual.notification.NotificationViewItem
import com.tebet.mojual.notification.NotificationViewLoading
import com.tebet.mojual.notification.NotificationViewTitle
import kotlinx.android.synthetic.main.activity_notification.*
import java.util.*
import kotlin.collections.ArrayList

class NotificationActivity : BaseActivity() {
    //save our FastAdapter
    private var fastItemAdapter: FastAdapter<NotificationViewItem>? = null
    private var footerAdapter: ItemAdapter<NotificationViewItem>? = null


    //endless scroll
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener
    private val allItems = ArrayList<NotificationNewResponse>()
    private var itemAdapter: ModelAdapter<NotificationNewResponse, NotificationViewItem>? = null

    override val contentLayoutId: Int
        get() = R.layout.activity_notification


    override fun onBackPressed() {
        val data = NotificationRequest()
        data.limit = NOTIFICATION_LIMIT_PER_REQUEST
        data.offset = 0
//        ApiService.createServiceNew(NotificationService::class.java).MarkAllNotificationAsRead(data)
//                .enqueue(object : ApiCallbackV2<NotificationsResponseV2>(mView) {
//                    override fun onFail(call: Call<*>?, e: ApiException) {
//                        super.onFail(call, e)
//                        finish()
//                        Timber.e(e)
//                    }
//
//                    override fun onSuccess(response: NotificationsResponseV2?) {
//                        super.onSuccess(response)
//                        finish()
//                    }
//                })
//

    }

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        setTitle(R.string.activity_noti_title)
        rvNotif?.visibility = View.VISIBLE
        rvNotif?.setHasFixedSize(true)
        rvNotif?.layoutManager = LinearLayoutManager(baseContext)

        //if you need multiple items for different models you can also do this be defining a Function which get's the model object and returns the item (extends IItem)
        itemAdapter = ModelAdapter { notification ->

            when (notification) {
                is NotificationTitle -> NotificationViewTitle(
                    notification
                )
                else -> NotificationViewItem(notification)
            }
        }

        //create our FastAdapter which will manage everything
        fastItemAdapter = FastAdapter.with<NotificationViewItem, ModelAdapter<NotificationNewResponse, NotificationViewItem>>(Arrays.asList(itemAdapter!!))
        fastItemAdapter?.withSelectable(true)

        //create our FooterAdapter which will manage the progress items
        footerAdapter = items<NotificationViewItem>()
        fastItemAdapter?.addAdapter(1, footerAdapter!!)

        //configure our fastAdapter
        fastItemAdapter?.withOnClickListener(OnClickListener<NotificationViewItem> { _, _, item, _ ->
            when {
                NotificationsResponseV2.STATUS_PENDING == item.model.notificationStatus -> {
                    val properties = Bundle().apply {
                        putString("code", item.model.templateCode)
                        putString("source", "notification_page")
                        putString("extraParams", Gson().toJson(item.model.extraParams))
                    }
//                    AnalyticEngine.action(TrackingEvent.NOTIFICATION_CLICK, properties)
                    showpDialog(true)
//                    ApiService.createServiceNew(NotificationService::class.java).getNotificationMarkAsRead(item.model.notificationId!!).enqueue(object : ApiCallbackV2<String>(mView) {
//                        override fun onSuccess(response: String?) {
//                            super.onSuccess(response)
//                            showpDialog(false)
//                            item.model.notificationStatus = NotificationsResponseV2.STATUS_READ
//                            fastItemAdapter?.notifyAdapterDataSetChanged()
//                            val data = item.model
//                            if (data.templateCode != null) {
//                                navigateTo(item.model)
//                            }
//                        }
//
//                        override fun onFail(call: Call<*>?, e: ApiException) {
//                            super.onFail(call, e)
//                            showpDialog(false)
//                            val data = item.model
//                            if (data.templateCode != null) {
//                                navigateTo(item.model)
//                            }
//                        }
//                    })
                    return@OnClickListener false
                }
                else -> {
                    val properties = Bundle().apply {
                        putString("code", item.model.templateCode)
                        putString("source", "notification_page")
                        putString("extraParams", Gson().toJson(item.model.extraParams))
                    }
//                    AnalyticEngine.action(TrackingEvent.NOTIFICATION_CLICK, properties)
                    val data = item.model
                    if (data.templateCode != null) {
//                        navigateTo(item.model)
                    }
                }
            }
            false
        })

        rvNotif?.layoutManager = LinearLayoutManager(this)
        rvNotif?.itemAnimator = DefaultItemAnimator()
        rvNotif?.adapter = fastItemAdapter
        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(footerAdapter) {
            override fun onLoadMore(currentPage: Int) {
                footerAdapter?.clear()
                footerAdapter?.add(NotificationViewLoading(null))
                loadData()
            }
        }
        rvNotif?.addOnScrollListener(endlessRecyclerOnScrollListener)

        //restore selections (this has to be done after the items were added
        savedInstanceState?.let {
            fastItemAdapter?.withSavedInstanceState(it)
        }
        loadData()
//        AnalyticEngine.page(TrackingEvent.NOTIFICATION_SCREEN)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        fastItemAdapter?.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun loadData() {
        val notificationRequest = NotificationRequest()
        notificationRequest.limit =
            NOTIFICATION_LIMIT_PER_REQUEST
        notificationRequest.offset = fastItemAdapter?.itemCount
        if (itemAdapter?.adapterItemCount == 0) {
            showpDialog(true)
        }
        nodataNotification?.visibility = View.GONE
//        ApiService.createServiceNew(NotificationService::class.java).getNotificationsV2(notificationRequest).enqueue(object : ApiCallbackV2<NotificationsResponseV2>(mView) {
//            override fun onSuccess(response: NotificationsResponseV2?) {
//                super.onSuccess(response)
//                Timber.d(GsonBuilder().setPrettyPrinting().create().toJson(response))
//                showpDialog(false)
//                rvNotif?.visibility = View.VISIBLE
//                if (response?.notifications == null || response.notifications?.size == 0) {
//                    if (itemAdapter?.adapterItemCount == 0) {
//                        rvNotif?.visibility = View.GONE
//                        nodataNotification?.visibility = View.VISIBLE
//                        return
//                    } else {
//                        if (footerAdapter != null) {
//                            footerAdapter?.clear()
//                        }
//                        return
//                    }
//                }
//                if (response.notifications!!.size < NOTIFICATION_LIMIT_PER_REQUEST) {
//                    if (footerAdapter != null) {
//                        footerAdapter?.clear()
//                    }
//                }
//
//                val items = ArrayList<NotificationViewItem>()
//                for (data in response.notifications!!) {
//                    if (itemAdapter?.adapterItemCount == 0 && items.size == 0) {
//                        itemAdapter?.add(
//                            NotificationTitle(
//                                if (data.isToday) getString(R.string.notification_title_today) else getString(
//                                    R.string.notification_title_older
//                                )
//                            )
//                        )
//                    } else if (!allItems.isEmpty() && allItems[allItems.size - 1].isToday && !data.isToday) {
//                        itemAdapter?.add(NotificationTitle(getString(R.string.notification_title_older)))
//                    }
//                    allItems.add(data)
//                    itemAdapter?.add(data)
//                }
//            }
//
//            override fun onFail(call: Call<*>?, e: ApiException) {
//                super.onFail(call, e)
//                showpDialog(false)
//                nodataNotification?.visibility = View.VISIBLE
//                showError(e)
//            }
//        })
    }

    companion object {
        internal const val NOTIFICATION_LIMIT_PER_REQUEST = 10
    }
}
