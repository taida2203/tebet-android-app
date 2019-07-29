package com.tebet.mojual.view.signup

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.databinding.ActivitySignUpInfoBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.home.HomeActivity
import com.tebet.mojual.view.signup.step1.SignUpInfoStep1
import com.tebet.mojual.view.signup.step1.SignUpInfoStep1Navigator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.io.File
import java.io.IOException
import androidx.lifecycle.Observer
import com.tebet.mojual.view.signup.step2.SignUpInfoStep2
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignUpInfo : BaseActivity<ActivitySignUpInfoBinding, SignUpViewModel>(), SignUpInfoStep1Navigator,
    HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
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

    var cameraCaptureData: MutableLiveData<String> = MutableLiveData()
        private set

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
        viewModel.baseErrorHandlerData.observe(this, Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
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

    override fun captureAvatar() {
        dispatchTakePictureIntent()
    }

    override fun captureEKTP() {
        dispatchTakePictureIntent()
    }

    lateinit var currentPhotoPath: String

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
    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
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
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO) {
            cameraCaptureData.postValue(currentPhotoPath)
        }
    }
}