package com.tebet.mojual.view.forgotpassword

import com.tebet.mojual.view.base.BaseActivityNavigator

interface ForgotPasswordNavigator: BaseActivityNavigator {
    fun openHomeScreen()
    fun dataValid(): Boolean
}
