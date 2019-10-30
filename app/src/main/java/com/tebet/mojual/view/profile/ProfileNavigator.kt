package com.tebet.mojual.view.profile

import com.tebet.mojual.data.models.Language
import com.tebet.mojual.view.base.BaseActivityNavigator

interface ProfileNavigator : BaseActivityNavigator{
    fun openLoginScreen()
    fun openChangeLanguageDialog()
    fun openChangePasswordScreen()
    fun openMyAssetScreen()
    fun changeLanguage(selectedItem: Language?)
    fun openPinCodeScreen()
    fun openTerm()
    fun openPrivacy()
    fun openContact()
    fun openFAQ()
}
