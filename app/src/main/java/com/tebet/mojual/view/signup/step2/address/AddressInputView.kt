package com.tebet.mojual.view.signup.step2.address

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.*
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.data.models.City
import com.tebet.mojual.databinding.LayoutAddressInputBinding
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2Model

@InverseBindingMethods(
    value = [InverseBindingMethod(
        type = AddressInputView::class,
        attribute = "bind:addressData",
        method = "getAddressValue"
    )]
)
class AddressInputView : LinearLayout {
    private lateinit var viewModel: SignUpInfoStep2Model
    private var mBinding: LayoutAddressInputBinding? = null
    var addressData: ObservableField<Address> = ObservableField(Address())
    lateinit var validator: Validator
    //    public String getFilterValue() {
    ////        return mBinding.filterPositionValue.getText().toString();
    //    }

    var cityAdapter: ArrayAdapter<String>? = null

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
//        mBinding?.setVariable(BR.addressData, addressData.get())
        mBinding?.setVariable(BR.view, this)
        validator = Validator(mBinding)
        validator.enableFormValidationMode()

        cityAdapter =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayListOf<String>())
        mBinding?.etCity?.setAdapter(cityAdapter)
        mBinding?.etCity?.threshold = 1
        mBinding?.etCity?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var currentItemString = cityAdapter?.getItem(position)
                addressData.get()?.city =
                    viewModel.cityLiveData.value?.firstOrNull { city -> city.fullName == currentItemString }?.fullName
            }
        mBinding?.etCity?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (viewModel.cityLiveData.value?.firstOrNull { city -> city.fullName == addressData.get()?.city } == null) {
                    addressData.get()?.city = ""
                }
            }
        }
        orientation = LinearLayout.HORIZONTAL
    }

    //    @BindingAdapter(value = ["bind:addressData"], requireAll = false)
    fun setAddressData(
        address: Address?,
        viewModel: SignUpInfoStep2Model,
        cities: List<City>?
    ) {
        addressData.set(address)
//        mBinding?.setVariable(BR.addressData, addressData.get())

        cityAdapter?.clear()
        cityAdapter?.addAll(cities?.map { city -> city.fullName } ?: arrayListOf())
        this.viewModel = viewModel
    }

    fun onChooseMapClick() {
        viewModel.onChooseMapClick(addressData)
    }

    fun validate(): Boolean {
        return validator.validate()
    }
}