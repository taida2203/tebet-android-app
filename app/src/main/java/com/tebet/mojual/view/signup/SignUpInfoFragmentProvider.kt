package com.tebet.mojual.view.signup

import com.tebet.mojual.view.signup.step1.SignUpInfoStep1
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2
import com.tebet.mojual.view.signup.step3.SignUpInfoStep3
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SignUpInfoFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun signUpInfoStep1Factory(): SignUpInfoStep1

    @ContributesAndroidInjector
    internal abstract fun signUpInfoStep2Factory(): SignUpInfoStep2

    @ContributesAndroidInjector
    internal abstract fun signUpInfoStep3Factory(): SignUpInfoStep3
}
