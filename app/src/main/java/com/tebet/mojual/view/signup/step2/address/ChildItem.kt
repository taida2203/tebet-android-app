package com.tebet.mojual.view.signup.step2.address

import android.widget.LinearLayout
import com.mindorks.placeholderview.ExpandablePlaceHolderView
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.expand.ChildPosition
import com.mindorks.placeholderview.annotations.expand.ParentPosition
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2Model

@Layout(R.layout.item_address_content)
class ChildItem(
    private val mExpandablePlaceHolderView: ExpandablePlaceHolderView,
    var address: Address,
    var viewModel: SignUpInfoStep2Model
) {

    @ParentPosition
    internal var mParentPosition: Int = 0
    @ChildPosition
    internal var mChildPosition: Int = 0
    @View(R.id.inputAddress)
    lateinit var inputAddress: AddressInputView
    //    @View(R.id.itemIcon)
    //    ImageView itemIcon;
    @View(R.id.mainView)
    internal var mainView: LinearLayout? = null

    @Resolve
    fun onResolved() {
        //        itemNameTxt.setText(childTitleList.get(mParentPosition).get(mChildPosition));
        //        itemIcon.setImageDrawable(mExpandablePlaceHolderView.getResources().getDrawable(R.drawable.signup_expand_btn));
        inputAddress?.setAddressData(inputAddress, address, viewModel)
    }

    @Click(R.id.mainView)
    fun onItemClick() {
        //        mExpandablePlaceHolderView.addChildView(mParentPosition, new ChildItem(mExpandablePlaceHolderView));
//        mExpandablePlaceHolderView.removeView(this)
    }

    fun validate() : Boolean {
        return inputAddress.validate()
    }
}
