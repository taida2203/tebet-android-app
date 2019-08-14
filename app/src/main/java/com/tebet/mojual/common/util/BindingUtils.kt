package com.tebet.mojual.common.util

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tebet.mojual.R
import com.tebet.mojual.common.view.AppEditText


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
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
                return
            }
            GlideApp.with(context).load(R.drawable.signup_avatar_blank)
                .placeholder(R.drawable.signup_avatar_blank)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
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
        fun setOnOkInSoftKeyboardListener(
            view: AppEditText,
            listener: OnOkInSoftKeyboardListener?
        ) {
            setOnOkInSoftKeyboardListener(view, listener)
        }
    }

    abstract class OnOkInSoftKeyboardListener {
        abstract fun onOkInSoftKeyboard()
    }
}
