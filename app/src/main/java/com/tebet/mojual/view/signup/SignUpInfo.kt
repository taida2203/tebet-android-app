package com.tebet.mojual.view.signup

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.common.view.dialog.DateDialog
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySignUpInfoBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.signup.step.SignUpInfoStep
import com.tebet.mojual.view.signup.step1.SignUpInfoStep1
import com.tebet.mojual.view.signup.step1.SignUpInfoStep1Navigator
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2
import com.tebet.mojual.view.signup.step3.SignUpInfoStep3
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignUpInfo : BaseActivity<ActivitySignUpInfoBinding, SignUpInfoViewModel>(), SignUpInfoStep1Navigator,
    HasSupportFragmentInjector, SignUpNavigator {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    lateinit var birthDayDialog: DateDialog

    private var topRightViewBinding: ItemHomeIconBinding? = null


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
    enum class SCREEN_STEP {
        STEP_1, STEP_2, STEP_3, STEP_FINISH
    }

    private var screenStep: SCREEN_STEP =
        SCREEN_STEP.STEP_1

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: SignUpInfoViewModel
        get() = ViewModelProviders.of(this, factory).get(SignUpInfoViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_info

    private var currentStepFragment: SignUpInfoStep<*, *>? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        topRightViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_home_icon, baseBinding.topRightHolder, true)
        title = "Sign Up"
        refreshScreenStep()
        viewDataBinding?.btnNext?.setOnClickListener {
            hideKeyboard()
            currentStepFragment?.let {
                if (!it.validate()) {
                    Toast.makeText(this@SignUpInfo, "Fill all required field !!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

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
        viewDataBinding?.btnBack?.background = ContextCompat.getDrawable(this, R.color.grey)
        viewDataBinding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        viewModel.baseErrorHandlerData.observe(this, Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.loadData()
    }

    override fun onBackPressed() {
        when (screenStep) {
            SCREEN_STEP.STEP_2 ->
                screenStep = SCREEN_STEP.STEP_1
            SCREEN_STEP.STEP_3, SCREEN_STEP.STEP_FINISH ->
                screenStep = SCREEN_STEP.STEP_2
            else -> SCREEN_STEP.STEP_1
        }
        refreshScreenStep()
    }

    private fun refreshScreenStep() {
        enableBackButton = true
        viewDataBinding?.btnBack?.visibility = View.VISIBLE

        viewDataBinding?.tvTitleStep1?.setTypeface(null, Typeface.NORMAL)
        viewDataBinding?.tvTitleStep2?.setTypeface(null, Typeface.NORMAL)
        viewDataBinding?.tvTitleStep3?.setTypeface(null, Typeface.NORMAL)
        viewDataBinding?.tvTitleStep1?.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        viewDataBinding?.tvTitleStep2?.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        viewDataBinding?.tvTitleStep3?.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

        when (screenStep) {
            SCREEN_STEP.STEP_1 -> {
                currentStepFragment = SignUpInfoStep1()
                openFragment(currentStepFragment as SignUpInfoStep1, R.id.placeHolderChild)
                enableBackButton = false
                viewDataBinding?.btnBack?.visibility = View.GONE
                viewDataBinding?.tvTitleStep1?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep1?.setTextColor(ContextCompat.getColor(this, R.color.green_dark))
            }

            SCREEN_STEP.STEP_2 -> {
                currentStepFragment = SignUpInfoStep2()
                openFragment(currentStepFragment as SignUpInfoStep2, R.id.placeHolderChild)
                viewDataBinding?.tvTitleStep2?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep2?.setTextColor(ContextCompat.getColor(this, R.color.green_dark))
            }

            SCREEN_STEP.STEP_3 -> {
                currentStepFragment = SignUpInfoStep3()
                openFragment(currentStepFragment as SignUpInfoStep3, R.id.placeHolderChild)
                viewDataBinding?.tvTitleStep3?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep3?.setTextColor(ContextCompat.getColor(this, R.color.green_dark))
            }
            else -> {
                viewDataBinding?.tvTitleStep3?.setTypeface(null, Typeface.BOLD)
                viewDataBinding?.tvTitleStep3?.setTextColor(ContextCompat.getColor(this, R.color.green_dark))
                viewModel.updateUserProfile()
            }
        }
    }

    override fun captureAvatar() {
        dispatchTakePictureIntent(REQUEST_TAKE_AVATAR)
    }

    override fun captureEKTP() {
        dispatchTakePictureIntent(REQUEST_TAKE_EKTP)
    }

    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private val REQUEST_TAKE_AVATAR = 1
    private val REQUEST_TAKE_EKTP = 2

    private fun dispatchTakePictureIntent(requestCode: Int) {
        currentPhotoPath = ""
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.tebet.mojual.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestCode)
                }
            }
        }
    }

    override fun openHomeScreen() {
        finish()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_AVATAR -> {
                if (!TextUtils.isEmpty(currentPhotoPath) && resultCode == Activity.RESULT_OK) {
                    viewModel.userProfile.avatarLocal = "file://$currentPhotoPath"
                    viewModel.uploadAvatar(currentPhotoPath)
                }
            }
            REQUEST_TAKE_EKTP -> {
                if (!TextUtils.isEmpty(currentPhotoPath) && resultCode == Activity.RESULT_OK) {
                    viewModel.userProfile.ktpLocal = "file://$currentPhotoPath"
                    viewModel.uploadEKTP(currentPhotoPath)
                }
            }
        }
    }

    override fun selectBirthDay() {
        birthDayDialog = DateDialog()
        birthDayDialog.setMaxDate(Calendar.getInstance())
        birthDayDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            viewModel.userProfile.birthday = "$dayOfMonth/$month/$year"
        }
        birthDayDialog.show(supportFragmentManager, "")
    }
}