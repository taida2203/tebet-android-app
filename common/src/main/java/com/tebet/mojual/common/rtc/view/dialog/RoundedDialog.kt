package com.tebet.mojual.common.rtc.view.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tebet.mojual.common.R
import kotlinx.android.synthetic.main.fragment_dialog_detail_cancel.*


@SuppressLint("ValidFragment")
open class RoundedDialog : DialogFragment, View.OnClickListener {

    protected var tvSingle: TextView? = null
    private var btnMultipleLeft: View? = null
    private var btnMultipleRight: View? = null
    protected var btnMultipleSingle: View? = null
    protected var btnClose: View? = null
    private var btnMultipleContainer: View? = null
    private var tvMultipleRight: TextView? = null
    private var tvMultipleLeft: TextView? = null
    private var tvHint : TextView? = null
    protected var okObject: Any? = null

    private var isShowing: Boolean = false
    private var firstButton: RoundedDialogButton? = null
    private var secondButton: RoundedDialogButton? = null

    private var contentMessage: String = ""
    private var hintMessage : String? = null
    private var contentTextView: TextView? = null

    protected var roundedDialogCallback: RoundedDialogCallback? = null
    protected var roundedDialogBehavior: RoundedDialogBehavior? = null

    internal var rotation: Float? = null
    protected var v: View? = null

    protected open val contentLayout: Int
        get() = R.layout.ok_cancel_default_layout

    @SuppressLint("ValidFragment")
    constructor(messageContent: String) {
        this.contentMessage = messageContent
    }

    constructor()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        try {
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        } catch (e: Exception) {
        }

        try {
            val dividerId = dialog.context.resources
                    .getIdentifier("android:id/titleDivider", null, null)
            val divider = dialog.findViewById<View>(dividerId)
            activity?.let { ContextCompat.getColor(it, android.R.color.transparent) }?.let { divider.setBackgroundColor(it) }
        } catch (e: Exception) {
        }
        v = LayoutInflater.from(activity)
                .inflate(R.layout.fragment_dialog_detail_cancel, container, false)
        return v
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float): Float {
        val resources = resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val placeHolder = v?.findViewById<FrameLayout>(R.id.placeHolder)
        activity?.layoutInflater?.inflate(contentLayout, placeHolder)

        btnMultipleSingle = v?.findViewById(R.id.btn_single)
        btnMultipleSingle?.setOnClickListener(this)
        btnMultipleLeft = v?.findViewById(R.id.btn_multiple_left)
        btnMultipleLeft?.setOnClickListener(this)
        btnMultipleRight = v?.findViewById(R.id.btn_multiple_right)
        btnMultipleRight?.setOnClickListener(this)
        btnClose = v?.findViewById(R.id.dialogTopClose)
        btnClose?.setOnClickListener(this)
//        dialogTopClose?.setOnClickListener(this)

        btnMultipleContainer = v?.findViewById(R.id.btn_multiple_container)

        tvSingle = v?.findViewById(R.id.tv_single)
        tvMultipleLeft = v?.findViewById(R.id.tv_multiple_left)
        tvMultipleRight = v?.findViewById(R.id.tv_multiple_right)
        contentTextView = v?.findViewById(R.id.message_content)
        tvHint = v?.findViewById(R.id.tv_Hint)

        //Add default "OK" button for dialog
        if (firstButton == null) {
            addFirstButton(RoundedDialogButton(resources.getString(R.string.general_button_ok), R.color.dialogColorOK))
        }

        if (rotation != null) {
            val rlRoot = v?.findViewById<View>(R.id.root)
            rlRoot?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rlRoot.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    rlRoot.x = -1 * rlRoot.width / 2 - convertDpToPixel(20f)
                    rlRoot.rotation = rotation!!
                }
            })
        }
        updateDialogMode()
    }

    override fun onStart() {
        super.onStart()
        if (rotation != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onClick(view: View) {
        val viewId = view.id
        when (viewId) {
            R.id.dialogTopClose -> {
                try {
                    dismiss()
                } catch (ignored: Exception) {
                }
            }

            R.id.btn_single -> {
                try {
                    dismiss()
                } catch (ignored: Exception) {
                }

                if (roundedDialogCallback != null) {
                    roundedDialogCallback!!.onFirstButtonClicked(okObject)
                }
            }
            R.id.btn_multiple_left -> {
                try {
                    dismiss()
                } catch (ignored: Exception) {
                }

                if (roundedDialogCallback != null) {
                    roundedDialogCallback!!.onFirstButtonClicked(okObject)
                }

            }
            R.id.btn_multiple_right -> {
                try {
                    dismiss()
                } catch (ignored: Exception) {
                }

                if (roundedDialogCallback != null) {
                    roundedDialogCallback!!.onSecondButtonClicked(okObject)
                }
            }
        }
    }

    fun setRoundedDialogCallback(roundedDialogCallback: RoundedDialogCallback): RoundedDialog {
        this.roundedDialogCallback = roundedDialogCallback
        return this
    }

    fun setRoundedDialogBehavior(roundedDialogBehavior: RoundedDialogBehavior): RoundedDialog {
        this.roundedDialogBehavior = roundedDialogBehavior
        return this
    }

    interface RoundedDialogCallback {
        fun onFirstButtonClicked(selectedValue: Any?)
        fun onSecondButtonClicked(selectedValue: Any?)
    }

    interface RoundedDialogBehavior {
        fun onDismiss()
    }

    protected fun updateDialogMode() {
        btnMultipleSingle?.visibility = View.GONE
        btnMultipleContainer?.visibility = View.GONE
        if (firstButton != null) {
            if (secondButton == null) {
                btnMultipleSingle?.visibility = View.VISIBLE
                btnMultipleContainer?.visibility = View.GONE
                tvSingle?.text = firstButton?.buttonText
                firstButton?.color?.let { btnMultipleSingle?.setBackgroundResource(it) }
            } else {
                btnMultipleSingle?.visibility = View.GONE
                btnMultipleContainer?.visibility = View.VISIBLE
                tvMultipleLeft?.text = firstButton?.buttonText
                firstButton?.color?.let { btnMultipleLeft?.setBackgroundResource(it) }
                if (secondButton != null) {
                    tvMultipleRight?.text = secondButton?.buttonText
                    secondButton?.color?.let { btnMultipleRight?.setBackgroundResource(it) }
                }
            }
        }
        if (contentTextView != null) {
            contentTextView?.text = contentMessage
        }
        if (tvHint != null){
            if (hintMessage != null){
                tvHint?.visibility = View.VISIBLE
                tvHint?.text = hintMessage
            }
        }
    }

    fun addFirstButton(button: RoundedDialogButton): RoundedDialog {
        firstButton = button
        return this
    }

    fun addSecondButton(button: RoundedDialogButton): RoundedDialog {
        secondButton = button
        return this
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isShowing) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (context != null && context is Activity) {
                if (!(context as Activity).isDestroyed) {
                    try {
                        super.show(manager, tag)
                        isShowing = true
                    } catch (ignored: Exception) {
                    }

                }
            } else {
                try {
                    super.show(manager, tag)
                    isShowing = true
                } catch (ignored: Exception) {
                }

            }
        } else {
            try {
                super.show(manager, tag)
                isShowing = true
            } catch (ignored: Exception) {
            }

        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        isShowing = false
        super.onDismiss(dialog)
        roundedDialogBehavior?.onDismiss()
    }

    fun setContentMessage(contentMessage: String): RoundedDialog {
        this.contentMessage = contentMessage
        return this
    }

    fun setHintMessage(hintMessage: String) : RoundedDialog{
        this.hintMessage = hintMessage
        return this
    }

    fun setRotation(rotation: Float?): RoundedDialog {
        this.rotation = rotation
        return this
    }

    fun setHorizontalButton(isHorizontal: Boolean) {
        if(isHorizontal) {
            btn_multiple_container.orientation = LinearLayout.HORIZONTAL
            btn_multiple_container.requestLayout()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE")
        //        super.onSaveInstanceState(outState);
    }

    fun hideOkButton() {
        btn_single_container?.visibility = View.GONE
    }
    fun hideCloseButton() {
        btnClose?.visibility = View.GONE
    }
    fun hideFirstButton() {
        btn_multiple_left?.visibility = View.GONE
    }
    fun hideSecondButton() {
        btn_multiple_right?.visibility = View.GONE
    }

}
