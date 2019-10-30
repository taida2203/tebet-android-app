package com.tebet.mojual.view.signup.step2

import com.tebet.mojual.data.models.Address
import com.tebet.mojual.view.base.BaseActivityNavigator

interface SignUpInfoStep2Navigator : BaseActivityNavigator {
    fun selectLocation(get: Address?)
    fun hideKeyboard()
}
