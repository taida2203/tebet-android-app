package com.tebet.mojual.view.message

import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Message
import com.tebet.mojual.databinding.FragmentMessageBinding
import com.tebet.mojual.view.base.BaseFragment
import com.tebet.mojual.view.home.Home

class MessageFragment : BaseFragment<FragmentMessageBinding, MessageViewModel>(),
    MessageNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: MessageViewModel
        get() = ViewModelProviders.of(this, factory).get(MessageViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_message

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewModel.loadData(false)
    }

    override fun openNotificationDetail(item: Message) {
        item.data?.let { baseActivity?.openFromNotification(it, item.read == false) }
    }
}
