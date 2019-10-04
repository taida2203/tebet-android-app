package com.tebet.mojual.common.util

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tebet.mojual.R
import com.tebet.mojual.common.adapter.scroll.RecyclerViewScrollCallback
import com.tebet.mojual.common.view.AppEditText
import com.tebet.mojual.data.models.Question
import com.tebet.mojual.view.qualityreject.question.QuestionInputView


class BindingUtils {
    companion object {
        @BindingAdapter(value = ["imageUrl", "imageError"], requireAll = false)
        @JvmStatic
        fun setImageUrl(imageView: ImageView, imageUrl: String?, imageError: Drawable?) {
            val context = imageView.context
            var imageErrorRes = imageError?.let { errorId -> errorId } ?: ContextCompat.getDrawable(context, R.drawable.signup_avatar_blank)
            imageUrl?.let {
                GlideApp.with(context).load(it)
                    .error(imageErrorRes)
                    .placeholder(imageErrorRes)
                    .centerCrop()
                    .into(imageView)
                return
            }
            GlideApp.with(context).load(imageErrorRes)
                .placeholder(imageErrorRes)
                .centerCrop()
                .into(imageView)
        }

        /**
         * @param recyclerView  RecyclerView to bind to RecyclerViewScrollCallback
         * @param visibleThreshold  The minimum number of items to have below your current scroll position before loading more.
         * @param resetLoadingState  Reset endless scroll listener when performing a new search
         * @param onScrolledListener    OnScrolledListener for RecyclerView scrolled
         */
        @BindingAdapter(value = ["visibleThreshold", "resetLoadingState", "onScrolledToBottom"], requireAll = false)
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


    @BindingAdapter("questions")
    fun LinearLayout.setQuestions(questions: List<Question>?) {
        val context = this@setQuestions.context
        questions?.forEach { question ->
            val myItem = QuestionInputView(context)
            myItem.data.set(question)
            this@setQuestions.addView(myItem)
        }
    }

    @BindingAdapter("onOkInSoftKeyboard")
    fun EditText.setOnOkInSoftKeyboardListener(listener: OnOkInSoftKeyboardListener?) {
        if (listener == null) {
            this@setOnOkInSoftKeyboardListener.setOnEditorActionListener(null)
        } else {
            this@setOnOkInSoftKeyboardListener.setOnEditorActionListener { _, _, _ ->
                // ... solution to receiving event
                listener.onOkInSoftKeyboard()
                true
            }
        }
    }

    @BindingAdapter("onOkInSoftKeyboard")
    fun AppEditText.setOnOkAppEditTextInSoftKeyboardListener(listener: OnOkInSoftKeyboardListener?) {
        if (listener == null) {
            this@setOnOkAppEditTextInSoftKeyboardListener.setOnEditorActionListener(null)
        } else {
            this@setOnOkAppEditTextInSoftKeyboardListener.setOnEditorActionListener { _, _, _ ->
                // ... solution to receiving event
                listener.onOkInSoftKeyboard()
                true
            }
        }
    }

    abstract class OnOkInSoftKeyboardListener {
        abstract fun onOkInSoftKeyboard()
    }
}
