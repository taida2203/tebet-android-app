package com.tebet.mojual.view.signup.step2.address

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.expand.*
import com.tebet.mojual.R

import java.util.ArrayList

@Parent
@SingleTop
@Layout(R.layout.item_address_title)
class ParentItem(private val mContext: Context, private val title: String, private val isExpandDefault: Boolean = false) {

    @ParentPosition
    internal var mParentPosition: Int = 0

    @View(R.id.itemNameTxt)
    internal var itemNameTxt: TextView? = null


    @View(R.id.itemIcon)
    internal var itemIcon: ImageView? = null

    @Toggle(R.id.mainView)
    @View(R.id.mainView)
    internal var mainView: LinearLayout? = null

    @Resolve
    fun onResolved() {
        itemIcon?.setImageDrawable(mContext.resources.getDrawable(R.drawable.signup_expand_btn))
        itemNameTxt?.text = title
        if(isExpandDefault){
            onExpand()
        }
    }

    @Expand
    fun onExpand() {
        itemIcon?.setImageDrawable(mContext.resources.getDrawable(R.drawable.signup_collapse_btn))
    }

    @Collapse
    fun onCollapse() {
        itemIcon?.setImageDrawable(mContext.resources.getDrawable(R.drawable.signup_expand_btn))
    }
}
