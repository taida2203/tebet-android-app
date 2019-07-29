package com.tebet.mojual.view.login

interface LoginNavigator {

    fun openLoginScreen()
    fun openRegistrationScreen()
    fun doAccountKitLogin(isRegistrationFLow: Boolean)
    fun openHomeScreen()
    fun onBackPressed()
    fun openUpdatePasswordScreen()
}
