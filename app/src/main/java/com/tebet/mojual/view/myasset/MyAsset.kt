package com.tebet.mojual.view.myasset

import android.app.Activity
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Asset
import com.tebet.mojual.databinding.ActivityMyAssetBinding
import com.tebet.mojual.view.base.BaseActivity

class MyAsset : BaseActivity<ActivityMyAssetBinding, MyAssetViewModel>(),
    MyAssetNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: MyAssetViewModel
        get() = ViewModelProviders.of(this, factory).get(MyAssetViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_my_asset

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        viewModel.loadData()
    }

    override fun itemSelected(item: Asset) {
    }
}
