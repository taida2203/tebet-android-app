package com.tebet.mojual.view.qualityreject.question

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.*
import androidx.databinding.library.baseAdapters.BR
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Question
import com.tebet.mojual.databinding.LayoutQuestionInputBinding

@InverseBindingMethods(
    value = [InverseBindingMethod(
        type = QuestionInputView::class,
        attribute = "data",
        method = "getValue"
    )]
)
class QuestionInputView : LinearLayout {
    private var mBinding: LayoutQuestionInputBinding? = null
    var data: ObservableField<Question> = ObservableField()
    lateinit var validator: Validator

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context)
    }

    private fun init(context: Context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_question_input, this, true)
//        mBinding?.setVariable(BR.data, data.get())
        mBinding?.setVariable(BR.view, this)
        validator = Validator(mBinding)
        validator.enableFormValidationMode()

        orientation = HORIZONTAL
    }

    @BindingAdapter(value = ["data"], requireAll = false)
    fun QuestionInputView.setQuestionData(question: Question?) {
        this@QuestionInputView.data.set(question)
    }

    fun validate(): Boolean {
        return validator.validate()
    }
}