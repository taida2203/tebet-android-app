package com.tebet.mojual.view.signup.step2.address

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.*
import br.com.ilhasoft.support.validation.Validator
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.BindingUtils
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.data.models.City
import com.tebet.mojual.databinding.LayoutAddressInputBinding
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2Model

@InverseBindingMethods(
    value = [InverseBindingMethod(
        type = AddressInputView::class,
        attribute = "data",
        method = "getValue"
    )]
)
class AddressInputView : LinearLayout {
    private var viewModel: SignUpInfoStep2Model? = null
    private var mBinding: LayoutAddressInputBinding? = null
    var data: ObservableField<Address> = ObservableField()
    var cities: ObservableList<City> = ObservableArrayList()
    lateinit var validator: Validator

    var cityAdapter: ArrayAdapter<String>? = null

    var onOkEditText: BindingUtils.OnOkInSoftKeyboardListener = object : BindingUtils.OnOkInSoftKeyboardListener() {
        override fun onOkInSoftKeyboard() {
            viewModel?.hideKeyboard()
        }
    }

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
//        mBinding?.setVariable(BR.data, data.get())
        mBinding?.setVariable(BR.view, this)
        validator = Validator(mBinding)
        validator.enableFormValidationMode()

        cityAdapter =
            ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayListOf<String>())
        mBinding?.etCity?.setAdapter(cityAdapter)
        mBinding?.etCity?.threshold = 1
        mBinding?.etCity?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val currentItemString = cityAdapter?.getItem(position)
                data.get()?.city =
                    viewModel?.cityLiveData?.value?.firstOrNull { city -> city.fullName == currentItemString }?.fullName
            }
        mBinding?.etCity?.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (viewModel?.cityLiveData?.value?.firstOrNull { city -> city.fullName == data.get()?.city } == null) {
                    data.get()?.city = ""
                }
            }
        }
        orientation = HORIZONTAL
    }

    companion object {
        @BindingAdapter(value = ["data", "cities", "viewModel"], requireAll = false)
        @JvmStatic
        fun setAddressData(view: AddressInputView, address: Address?, cities: List<City>?, viewModel: SignUpInfoStep2Model?) {
            view.data.set(address)
            view.viewModel = viewModel
            cities?.let { view.cities.addAll(it) }
            view.cityAdapter?.clear()
            view.cityAdapter?.addAll(cities?.map { city -> city.fullName } ?: arrayListOf())
//        mBinding?.setVariable(BR.data, data.get())
//        if (viewModel != null) {
//            this.viewModel = viewModel
//        }
        }
    }

    fun onChooseMapClick() {
        viewModel?.onChooseMapClick(data)
    }

    fun validate(): Boolean {
        return validator.validate()
    }
}