package com.tebet.mojual.view.qualitycontainer

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import co.common.view.dialog.RoundedCancelOkDialog
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedOkDialog
import com.tebet.mojual.R
import com.tebet.mojual.common.services.DigitalFootPrintServices
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.ActivityQualityAddContainerBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.help.QualityHelp
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber


class QualityAddContainer :
    BaseActivity<ActivityQualityAddContainerBinding, QualityAddContainerViewModel>(),
    QualityAddContainerNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: QualityAddContainerViewModel
        get() = ViewModelProviders.of(this, factory).get(QualityAddContainerViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_quality_add_container

    private lateinit var validator: Validator
    private var topRightViewBinding: ItemHomeIconBinding? = null

    companion object {
        private const val RC_CAMERA_AND_LOCATION = 300
        private const val GPS_ENABLE = 301
        private const val WIFI_MANUAL = 302
    }

    private var gpsDialog: RoundedDialog? = null
    private var turnOffIOTDialog: RoundedDialog? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        enableBackButton = true
        validator = Validator(viewDataBinding)
        validator.enableFormValidationMode()
        title = getString(R.string.quality_add_container_title)
        topRightViewBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_home_icon,
                baseBinding.topRightHolder,
                true
            )
        intent.getSerializableExtra("EXTRA_ORDER")?.let {
            viewModel.order.set(it as Order)
            if (!viewModel.order.get()?.orderCode.isNullOrBlank()) title = String.format(getString(R.string.check_quality_add_container_order), viewModel.order.get()?.orderCode)
        }
        viewModel.loadData()
    }

    private fun buildAlertMessageNoGps() {
        if (gpsDialog == null) {
            gpsDialog = RoundedCancelOkDialog("Your GPS seems to be disabled, do you want to enable it?").setRoundedDialogCallback(
                    object : RoundedDialog.RoundedDialogCallback {
                        override fun onFirstButtonClicked(selectedValue: Any?) {
                        }

                        override fun onSecondButtonClicked(selectedValue: Any?) {
                            startActivityForResult(
                                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                GPS_ENABLE
                            )
                        }
                    })
        }
        gpsDialog?.show(supportFragmentManager, "")
    }

    override fun openConfirmScreen(dataResponse: Order) {
        try {
            val i = DigitalFootPrintServices.newIntent(this, orderId = dataResponse.orderId, orderCode = dataResponse.orderCode)
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> applicationContext.startForegroundService(i) // https://www.fabric.io/masbro/android/apps/co.masbro.consumer/issues/5ac742c036c7b23527af337b?time=last-seven-days
                else -> applicationContext.startService(i)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        turnOffIOTDialog?.setRoundedDialogCallback(object : RoundedDialog.RoundedDialogCallback {
            override fun onFirstButtonClicked(selectedValue: Any?) {
                navigateToConfirmScreen(dataResponse)
            }

            override fun onSecondButtonClicked(selectedValue: Any?) {
            }
        })
        if (turnOffIOTDialog?.isVisible != true) {
            navigateToConfirmScreen(dataResponse)
        }
    }

    private fun navigateToConfirmScreen(dataResponse: Order) {
        intent.putExtra("EXTRA_ORDER", dataResponse)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun dataValid(): Boolean {
        hideKeyboard()
        return validator.validate()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    override fun requestLocationAndConnectIOT() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(this, *arrayOf(ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE, CHANGE_WIFI_STATE, CHANGE_NETWORK_STATE))) {
                val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps()
                } else {
                    viewModel.connectIOT()
                }
            } else {
                // Do not have permissions, request them now
                EasyPermissions.requestPermissions(this, "Require permission", RC_CAMERA_AND_LOCATION, *arrayOf(ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE, CHANGE_WIFI_STATE, CHANGE_NETWORK_STATE))
            }
        } else {
            viewModel.connectIOT()
        }
    }

    override fun connectIOTManual() {
        startActivityForResult(Intent(Settings.ACTION_WIFI_SETTINGS), WIFI_MANUAL)
    }

    override fun showTurnOffIOTDialog() {
        if (turnOffIOTDialog == null) {
            turnOffIOTDialog = RoundedOkDialog(getString(R.string.check_quality_add_container_turn_off_iot))
        }
        turnOffIOTDialog?.show(supportFragmentManager, "showTurnOffIOTDialog")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE -> requestLocationAndConnectIOT()
            WIFI_MANUAL -> viewModel.connectIOT()
        }
    }

    override fun openQualityHelpScreen() {
        startActivity(Intent(this, QualityHelp::class.java))
    }
}
