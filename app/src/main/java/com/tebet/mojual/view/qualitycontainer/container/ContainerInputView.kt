package com.tebet.mojual.view.qualitycontainer.container

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.*
import br.com.ilhasoft.support.validation.Validator
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.data.models.ContainerWrapper
import com.tebet.mojual.databinding.LayoutContainerInputBinding
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2Model
import me.tatarka.bindingcollectionadapter2.ItemBinding

@InverseBindingMethods(
    value = [InverseBindingMethod(
        type = ContainerInputView::class,
        attribute = "bind:data",
        method = "getValue"
    )]
)
class ContainerInputView : LinearLayout {
    private var mBinding: LayoutContainerInputBinding? = null
    var data: ObservableField<ContainerWrapper> = ObservableField()

    var itemWeights: ObservableList<Int> = ObservableArrayList()
    var itemBinding: ItemBinding<Asset> = ItemBinding.of(BR.item, R.layout.item_container_dropdown)
    var itemWeightBinding: ItemBinding<Int> = ItemBinding.of(BR.item, R.layout.item_container_weight_dropdown)

    lateinit var validator: Validator
    //    public String getFilterValue() {
    ////        return mBinding.filterPositionValue.getText().toString();
    //    }

    val addressValue: ContainerWrapper
        get() = data.get()!!

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
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_container_input, this, true)
//        mBinding?.setVariable(BR.data, data.get())
        mBinding?.setVariable(BR.view, this)
        validator = Validator(mBinding)
        validator.enableFormValidationMode()

        orientation = LinearLayout.HORIZONTAL
    }

    companion object {
        @BindingAdapter(value = ["bind:data"], requireAll = false)
        @JvmStatic
        fun setAddressData(view: ContainerInputView, containerWrapper: ContainerWrapper?) {
            view.data.set(containerWrapper)
            containerWrapper?.weightList?.let {
                view.itemWeights.clear()
                view.itemWeights.addAll(it)
            }
//        mBinding?.setVariable(BR.data, data.get())
//        if (viewModel != null) {
//            this.viewModel = viewModel
//        }
        }
    }

    fun validate(): Boolean {
        return validator.validate()
    }
}