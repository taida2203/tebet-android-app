package com.tebet.mojual.view.qualitycontainer

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import androidx.databinding.library.baseAdapters.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.ActivityQualityAddContainerBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Build
import co.common.view.dialog.RoundedDialog
import co.common.view.dialog.RoundedOkDialog
import com.tebet.mojual.common.services.DigitalFootPrintServices
import pub.devrel.easypermissions.AfterPermissionGranted
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

    @Inject
    lateinit var sensor: Sensor

    companion object {
        private const val RC_CAMERA_AND_LOCATION = 300
    }

    lateinit var mWifiManager: WifiManager
    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        viewDataBinding?.sensor = sensor
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
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        registerReceiver(mWifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            requestLocationAndConnectIOT()
        } else {
            connectIOT()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mWifiScanReceiver)
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
        RoundedOkDialog(getString(R.string.check_quality_add_container_turn_off_iot)).setRoundedDialogCallback(
            object : RoundedDialog.RoundedDialogCallback {
                override fun onFirstButtonClicked(selectedValue: Any?) {
                    if (!sensor.isConnected) {
                        intent.putExtra("EXTRA_ORDER", dataResponse)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }

                override fun onSecondButtonClicked(selectedValue: Any?) {
                }
            }).show(supportFragmentManager, "")
    }

    private var mWifiScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success == true) {
                if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    sensor.isEnabled = mWifiManager.scanResults.firstOrNull { it.SSID.contains(sensor.sensorSSID) } != null
                    if (sensor.isEnabled) sensor.connect()
                }
            } else {
                sensor.connect()
            }
        }
    }

    override fun reTryConnectIOT() {
        mWifiManager.startScan()
    }

    override fun dataValid(): Boolean {
        hideKeyboard()
        return validator.validate()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    override fun requestLocationAndConnectIOT() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
           connectIOT()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Require permission", RC_CAMERA_AND_LOCATION, ACCESS_FINE_LOCATION)
        }
    }

    private fun connectIOT() {
        if (!sensor.isEnabled) {
            reTryConnectIOT()
        } else if (!sensor.isConnected) {
            sensor.connect()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
