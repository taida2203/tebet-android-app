package com.tebet.mojual.notification

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mikepenz.fastadapter.FastAdapter
import com.tebet.mojual.R
import com.tebet.mojual.data.remote.NotificationNewResponse

class NotificationViewTitle(notification: NotificationNewResponse) : NotificationViewItem(notification), INotification {

    override fun getType(): Int {
        return R.id.notification_title_id
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_list_notif_title
    }

    override fun getViewHolder(v: View): FastAdapter.ViewHolder<*> {
        return ViewTitleHolder(v)
    }

    class ViewTitleHolder(view: View) : FastAdapter.ViewHolder<NotificationViewTitle>(view) {
        var title: TextView? = null

        init {
//            ButterKnife.bind(this, view)
            title = view.findViewById<View>(R.id.tvNotificationTitle) as TextView?
        }

        override fun bindView(item: NotificationViewTitle, payloads: List<Any>) {
            val ctx = itemView.context
            title!!.setTextColor(ContextCompat.getColor(ctx, R.color.darker_blue_notify))
            if (ctx.getString(R.string.notification_title_older) == item.model.title) {
                title!!.setTextColor(ContextCompat.getColor(ctx, R.color.grey_text_notify))
            }
//            title!!.typeface = Typeface.DEFAULT_BOLD
            title!!.text = item.model.title
        }

        override fun unbindView(item: NotificationViewTitle) {
            title!!.text = null
        }
    }
}
