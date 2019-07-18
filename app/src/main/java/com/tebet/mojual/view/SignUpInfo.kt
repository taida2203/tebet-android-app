package com.tebet.mojual.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tebet.mojual.R
import com.tebet.mojual.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sign_up_info.*
import kotlinx.android.synthetic.main.activity_sign_up_password.*
import kotlinx.android.synthetic.main.activity_sign_up_password.btnNext

class SignUpInfo : BaseActivity() {
    enum class SCREEN_STEP {
        STEP_1, STEP_2, STEP_3, STEP_FINISH
    }

    var screenStep: SCREEN_STEP = SCREEN_STEP.STEP_1

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_info

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        refreshScreenStep()
        btnNext.setOnClickListener {
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
        btnBack.setOnClickListener {
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
        btnBack.visibility = View.VISIBLE
        when (screenStep) {
            SCREEN_STEP.STEP_1 -> {
                openFragment(SignUpInfoStep1(), R.id.placeHolderChild)
                btnBack.visibility = View.GONE
            }
            SCREEN_STEP.STEP_2 ->
                openFragment(SignUpInfoStep2(), R.id.placeHolderChild)
            SCREEN_STEP.STEP_3 ->
                openFragment(SignUpInfoStep3(), R.id.placeHolderChild)
            else -> {
                this@SignUpInfo.finish()
                startActivity(Intent(this@SignUpInfo, HomeActivity::class.java))
            }
        }
    }
}