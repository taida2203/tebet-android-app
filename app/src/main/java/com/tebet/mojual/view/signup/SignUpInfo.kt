package com.tebet.mojual.view.signup

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySignUpInfoBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.home.HomeActivity

class SignUpInfo : BaseActivity<ActivitySignUpInfoBinding, SignUpViewModel>() {
    enum class SCREEN_STEP {
        STEP_1, STEP_2, STEP_3, STEP_FINISH
    }

    var screenStep: SCREEN_STEP =
        SCREEN_STEP.STEP_1

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpViewModel
        get() = ViewModelProviders.of(this, factory).get(SignUpViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_info

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        title = "Sign Up"
        refreshScreenStep()
        viewDataBinding?.btnNext?.setOnClickListener {
            when (screenStep) {
                SCREEN_STEP.STEP_1 ->
                    screenStep = SCREEN_STEP.STEP_2
                SCREEN_STEP.STEP_2 ->
                    screenStep = SCREEN_STEP.STEP_3
                SCREEN_STEP.STEP_3 ->
                    screenStep = SCREEN_STEP.STEP_FINISH
                else -> SCREEN_STEP.STEP_1
            }

            refreshScreenStep()
        }
        viewDataBinding?.btnBack?.background = ContextCompat.getDrawable(this,R.drawable.rounded_btn_grey)
        viewDataBinding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        when (screenStep) {
            SCREEN_STEP.STEP_2 ->
                screenStep = SCREEN_STEP.STEP_1
            SCREEN_STEP.STEP_3 ->
                screenStep = SCREEN_STEP.STEP_2
            else -> SCREEN_STEP.STEP_1
        }
        refreshScreenStep()
    }

    private fun refreshScreenStep() {
        viewDataBinding?.btnBack?.visibility = View.VISIBLE

        viewDataBinding?.tvTitleStep1?.setTypeface(null, Typeface.NORMAL)
        viewDataBinding?.tvTitleStep2?.setTypeface(null, Typeface.NORMAL)
        viewDataBinding?.tvTitleStep3?.setTypeface(null, Typeface.NORMAL)

        when (screenStep) {
            SCREEN_STEP.STEP_1 -> {
                openFragment(SignUpInfoStep1(), R.id.placeHolderChild)
                viewDataBinding?.btnBack?.visibility = View.GONE
                viewDataBinding?.tvTitleStep1?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep1?.setTextColor(ContextCompat.getColor(this,R.color.dark_green))
            }

            SCREEN_STEP.STEP_2 -> {
                openFragment(SignUpInfoStep2(), R.id.placeHolderChild)
                viewDataBinding?.tvTitleStep2?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep2?.setTextColor(ContextCompat.getColor(this,R.color.dark_green))
            }

            SCREEN_STEP.STEP_3 -> {
                openFragment(SignUpInfoStep3(), R.id.placeHolderChild)
                viewDataBinding?.tvTitleStep3?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep3?.setTextColor(ContextCompat.getColor(this,R.color.dark_green))
            }

            else -> {
                this@SignUpInfo.finish()
                startActivity(Intent(this@SignUpInfo, HomeActivity::class.java))
            }
        }
    }
}