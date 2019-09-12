package com.tebet.mojual.common.util

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.scroll.RecyclerViewScrollCallback
import com.tebet.mojual.common.view.AppEditText
import timber.log.Timber


class BindingUtils {
    companion object {
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun setImageUrl(imageView: ImageView, url: String?) {
            val context = imageView.context
            url?.let {
                GlideApp.with(context).load(it)
                    .error(R.drawable.signup_avatar_blank)
                    .placeholder(R.drawable.signup_avatar_blank)
                    .centerCrop()
                    .into(imageView)
                return
            }
            GlideApp.with(context).load(R.drawable.signup_avatar_blank)
                .placeholder(R.drawable.signup_avatar_blank)
                .centerCrop()
                .into(imageView)
        }

        @BindingAdapter("onOkInSoftKeyboard")
        @JvmStatic
        fun setOnOkInSoftKeyboardListener(
            view: EditText,
            listener: OnOkInSoftKeyboardListener?
        ) {
            if (listener == null) {
                view.setOnEditorActionListener(null)
            } else {
                view.setOnEditorActionListener { v, actionId, event ->
                    // ... solution to receiving event
                    listener.onOkInSoftKeyboard()
                    true
                }
            }
        }

        @BindingAdapter("onOkInSoftKeyboard")
        @JvmStatic
        fun setOnOkAppEditTextInSoftKeyboardListener(
            view: AppEditText,
            listener: OnOkInSoftKeyboardListener?
        ) {
            setOnOkInSoftKeyboardListener(view, listener)
        }

        /**
         * @param recyclerView  RecyclerView to bind to RecyclerViewScrollCallback
         * @param visibleThreshold  The minimum number of items to have below your current scroll position before loading more.
         * @param resetLoadingState  Reset endless scroll listener when performing a new search
         * @param onScrolledListener    OnScrolledListener for RecyclerView scrolled
         */
        @BindingAdapter(value = *arrayOf("visibleThreshold", "resetLoadingState", "onScrolledToBottom"), requireAll = false)
        @JvmStatic
        fun setRecyclerViewScrollCallback(recyclerView: RecyclerView, visibleThreshold: Int, resetLoadingState: Boolean,
                                          onScrolledListener: RecyclerViewScrollCallback.OnScrolledListener) {

            val callback = RecyclerViewScrollCallback
                .Builder(recyclerView.layoutManager!!)
                .visibleThreshold(visibleThreshold)
                .onScrolledListener(onScrolledListener)
                .resetLoadingState(resetLoadingState)
                .build()

            recyclerView.clearOnScrollListeners()
            recyclerView.addOnScrollListener(callback)
        }
    }

    abstract class OnOkInSoftKeyboardListener {
        abstract fun onOkInSoftKeyboard()
    }
}
