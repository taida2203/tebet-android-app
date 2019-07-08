package com.tebet.mojual.notification

import android.view.View
import android.widget.ProgressBar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils
import com.tebet.mojual.data.remote.NotificationNewResponse

class NotificationViewLoading(notification: NotificationNewResponse?) : NotificationViewItem(notification), INotification {
    override fun getType(): Int {
        return com.mikepenz.library_extensions.R.id.progress_item_id
    }

    override fun getLayoutRes(): Int {
        return com.mikepenz.library_extensions.R.layout.progress_item
    }

    override fun getViewHolder(v: View): FastAdapter.ViewHolder<*> {
        return ViewLoadingHolder(v)
    }

    override fun bindView(holder: FastAdapter.ViewHolder<*>, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.itemView.setBackgroundResource(FastAdapterUIUtils.getSelectableBackground(holder.itemView.context))
    }

    protected class ViewLoadingHolder(view: View) : FastAdapter.ViewHolder<NotificationViewLoading>(view) {
        protected var progressBar: ProgressBar

        override fun unbindView(item: NotificationViewLoading) {
        }

        override fun bindView(item: NotificationViewLoading, payloads: MutableList<Any>) {
        }

        init {
            progressBar = view.findViewById<View>(com.mikepenz.library_extensions.R.id.progress_bar) as ProgressBar
        }
    }
}