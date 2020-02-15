package com.tebet.mojual.view.qualitydetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedDialogButton
import androidx.databinding.library.baseAdapters.BR
import co.common.view.dialog.SingleChoiceDialog
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.data.models.OrderContainer
import com.tebet.mojual.data.models.OrderDetail
import com.tebet.mojual.data.models.OrderDocument
import com.tebet.mojual.data.models.enumeration.DocumentType
import com.tebet.mojual.databinding.FragmentOrderDetailBinding
import com.tebet.mojual.view.base.BaseFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding, OrderDetailViewModel>(),
    OrderDetailNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: OrderDetailViewModel
        get() = ViewModelProviders.of(this, factory).get(OrderDetailViewModel::class.java)

    override val layoutId: Int
        get() = R.layout.fragment_order_detail

    private lateinit var validator: Validator

    var order: Order? = null
    var documentTypeDialog: SingleChoiceDialog<DocumentType>? = null
    var documentDescriptionDialog: InputTextDialog? = null

    companion object {
        const val REQUEST_TAKE_DOCUMENT = 999
        fun newInstance(dataResponse: Order): OrderDetailFragment {
            val fragment = OrderDetailFragment()
            fragment.order = dataResponse
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        viewModel.order.set(order?.let { OrderDetail(it) })
        viewModel.loadData()
    }

    override fun itemSelected(item: OrderContainer) {
    }

    override fun validate(): Boolean {
        return validator.validate()
    }

    override fun openOrderDetailScreen(it: OrderDetail) {
        viewModel.loadData()
    }

    override fun openBankConfirmScreen(order: OrderDetail, selectedItems: List<OrderContainer>) {
        (activity as OrderDetailActivity).viewModel.onBankConfirmClick(order, selectedItems)
    }

    override fun showRejectConfirm() {
        activity?.supportFragmentManager?.let {
            RoundedDialog(getString(R.string.order_detail_dialog_reject))
                .addFirstButton(RoundedDialogButton(getString(R.string.general_btn_no), R.drawable.rounded_bg_button_trans))
                .addSecondButton(RoundedDialogButton(getString(R.string.general_btn_yes)))
                .setRoundedDialogCallback(
                    object : RoundedDialog.RoundedDialogCallback {
                        override fun onFirstButtonClicked(selectedValue: Any?) {
                        }

                        override fun onSecondButtonClicked(selectedValue: Any?) {
                            viewModel.rejectOrder()
                        }
                    }).show(it, "")
        }
    }

    override fun openReasonScreen(order: OrderDetail, selectedItems: List<OrderContainer>) {
        (activity as OrderDetailActivity).viewModel.onReasonClick(order, selectedItems)
    }

    override fun showConfirmDialog(selectedItems: List<OrderContainer>) {
        activity?.supportFragmentManager?.let { context ->
            RoundedDialog(
                String.format(
                    getString(R.string.order_detail_dialog_confirm),
                    selectedItems.filter { it.isSelected }.map { it.assetCode }.joinToString()
                )
            )
                .addFirstButton(RoundedDialogButton(getString(R.string.general_btn_no), R.drawable.rounded_bg_button_trans))
                .addSecondButton(RoundedDialogButton(getString(R.string.general_btn_yes)))
                .setRoundedDialogCallback(
                object : RoundedDialog.RoundedDialogCallback {
                    override fun onFirstButtonClicked(selectedValue: Any?) {
                    }

                    override fun onSecondButtonClicked(selectedValue: Any?) {
                        viewModel.approveOrder(selectedItems)
                    }
                }).show(context, "")
        }
    }

    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_DOCUMENT -> {
                if (!TextUtils.isEmpty(currentPhotoPath) && resultCode == Activity.RESULT_OK) {
                    documentDescriptionDialog =
                        InputTextDialog().setCallback(object : InputTextDialog.DialogCallback {
                            override fun onCancel() {
                                hideKeyboard()
                            }

                            override fun onOk(text: String?) {
                                hideKeyboard()
                                viewModel.selectedDocument?.description = text
                                viewModel.uploadDocument(currentPhotoPath)
                            }
                        })
                    fragmentManager?.let { documentDescriptionDialog?.show(it, "") }
                }
            }
        }
    }

    override fun uploadDocument() {
        documentTypeDialog = DocumentTypeDialog().setCallback(object :
            SingleChoiceDialog.SingleChoiceDialogCallback<DocumentType> {
            override fun onCancel() {
            }

            override fun onOk(selectedItem: DocumentType?) {
                if (selectedItem != null) {
                    viewModel.selectedDocument?.type = selectedItem.name
                    dispatchTakePictureIntent(REQUEST_TAKE_DOCUMENT)
                }
            }
        })
        fragmentManager?.let { documentTypeDialog?.show(it, "") }
    }

    override fun documentSelected(item: OrderDocument) {
        fragmentManager?.let { DocumentDialog(item).show(it, "") }
    }

    private fun dispatchTakePictureIntent(requestCode: Int, isFront: Boolean = false) {
        currentPhotoPath = ""
        val mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (isFront) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                activity!!.intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT)
                activity!!.intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
                activity!!.intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            } else {
                activity!!.intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
            }
        }
        mIntent.also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
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
                        activity!!,
                        "com.tebet.mojual.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestCode)
                }
            }
        }
    }
}
