package com.tebet.mojual.view.signup.step2

import androidx.databinding.ObservableField
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.view.signup.step.SignUpInfoStepViewModel

class SignUpInfoStep2Model(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    SignUpInfoStepViewModel<SignUpInfoStep2Navigator>(dataManager, schedulerProvider) {
    var address: ObservableField<Address>? = null
    override var userProfile: ObservableField<UserProfile> = super.userProfile

    fun onChooseMapClick(address: ObservableField<Address>) {
        this.address = address
        navigator.selectLocation(address.get())
    }
}