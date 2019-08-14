package com.tebet.mojual.view.history

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.FragmentHistoryBinding
import com.tebet.mojual.view.base.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding, HistoryViewModel>(), HistoryNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: HistoryViewModel
        get() = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_history

    private lateinit var validator: Validator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.loadData()
    }

    override fun itemSelected(item: Order) {
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun validate(): Boolean {
        return validator.validate()
    }
}
