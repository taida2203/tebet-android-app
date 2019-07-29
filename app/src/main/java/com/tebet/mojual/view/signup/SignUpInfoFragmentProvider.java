package com.tebet.mojual.view.signup;

import com.tebet.mojual.view.signup.step1.SignUpInfoStep1;
import com.tebet.mojual.view.signup.step2.SignUpInfoStep3;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SignUpInfoFragmentProvider {

    @ContributesAndroidInjector()
    abstract SignUpInfoStep1 signUpInfoStep1Factory();

    @ContributesAndroidInjector()
    abstract SignUpInfoStep3 signUpInfoStep2Factory();

    @ContributesAndroidInjector()
    abstract SignUpInfoStep3 signUpInfoStep3Factory();
}
