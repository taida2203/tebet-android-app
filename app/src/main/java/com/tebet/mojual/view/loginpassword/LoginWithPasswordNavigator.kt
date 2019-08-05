package com.tebet.mojual.view.loginpassword

import com.tebet.mojual.view.base.BaseActivityNavigator

interface LoginWithPasswordNavigator : BaseActivityNavigator{
    fun openHomeScreen()
    fun openForgotPasswordScreen()
    fun openRegistrationScreen()
    fun dataValid(): Boolean
}
