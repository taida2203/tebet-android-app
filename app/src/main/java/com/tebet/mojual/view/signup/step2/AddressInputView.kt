package com.tebet.mojual.view.signup.step2

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.*
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.databinding.LayoutAddressInputBinding

@InverseBindingMethods(
    value = [InverseBindingMethod(
        type = AddressInputView::class,
        attribute = "bind:addressData",
        method = "getAddressValue"
    )]
)
class AddressInputView : LinearLayout {
    private var mBinding: LayoutAddressInputBinding? = null
    var addressData: ObservableField<Address> = ObservableField(Address())
    //    public String getFilterValue() {
    ////        return mBinding.filterPositionValue.getText().toString();
    //    }
    val addressValue: Address
        get() = addressData.get()!!

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
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_address_input, this, true)
        mBinding?.setVariable(BR.addressData, addressData.get())
        orientation = LinearLayout.HORIZONTAL
    }

    @BindingAdapter(value = ["bind:addressData"], requireAll = false)
    fun setAddressData(positionView: AddressInputView?, address: Address) {
        addressData.set(address)
        mBinding?.setVariable(BR.addressData, addressData.get())
    }

    companion object {
    }
}