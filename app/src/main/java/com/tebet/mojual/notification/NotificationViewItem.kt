package com.tebet.mojual.notification

import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.ModelAbstractItem
import com.tebet.mojual.R
import com.tebet.mojual.data.remote.NotificationNewResponse
import com.tebet.mojual.data.remote.NotificationsResponseV2

open class NotificationViewItem(notification: NotificationNewResponse?) : ModelAbstractItem<NotificationNewResponse, NotificationViewItem, FastAdapter.ViewHolder<*>>(notification) {
    override fun getType(): Int {
        return R.id.notification_item_id
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_list_notif
    }

    override fun getViewHolder(v: View): FastAdapter.ViewHolder<*> {
        return ViewHolder(v)
    }

    open class ViewHolder(var view: View) : FastAdapter.ViewHolder<NotificationViewItem>(view) {
        protected var name: TextView? = null
        protected var description: TextView? = null
        protected var notiTitle: TextView? = null
        protected var redBadge: View? = null

        init {
            name = view.findViewById<View>(R.id.tvNotifDate) as TextView?
            description = view.findViewById<View>(R.id.tvNotifContent) as TextView?
            notiTitle = view.findViewById<View>(R.id.tvNotifType) as TextView?
            redBadge = view.findViewById<View>(R.id.clNewNotify) as View?
        }

        override fun bindView(item: NotificationViewItem, payloads: List<Any>) {
            val ctx = itemView.context
            notiTitle!!.text = item.model.title
            name!!.text = item.model.toHumanRead()
            description!!.text = item.model.content
            redBadge!!.visibility = if (item.model.notificationStatus == NotificationsResponseV2.STATUS_PENDING) View.VISIBLE else View.INVISIBLE
        }

        override fun unbindView(item: NotificationViewItem) {
            notiTitle!!.text = null
            name!!.text = null
            description!!.text = null
        }
    }
}
