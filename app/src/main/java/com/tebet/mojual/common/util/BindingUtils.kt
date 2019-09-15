package com.tebet.mojual.common.util

import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.scroll.RecyclerViewScrollCallback
import com.tebet.mojual.common.view.AppEditText
import com.tebet.mojual.data.models.Question
import com.tebet.mojual.view.qualityreject.question.QuestionInputView


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

        @BindingAdapter("questions")
        @JvmStatic
        fun setQuestions(layout: LinearLayout, questions: List<Question>?) {
            val context = layout.context
            questions?.forEach { question ->
                val myItem = QuestionInputView(context)
                myItem.data.set(question)
                layout.addView(myItem)
            }
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
