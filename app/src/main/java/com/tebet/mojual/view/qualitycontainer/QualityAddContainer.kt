package com.tebet.mojual.view.qualitycontainer

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import br.com.ilhasoft.support.validation.Validator
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.ActivityQualityAddContainerBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import javax.inject.Inject


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
            title = String.format(
                getString(R.string.check_quality_add_container_order),
                viewModel.order.get()?.orderCode
            )
        }
        viewModel.loadData()
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        registerReceiver(mWifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mWifiScanReceiver)
    }

    override fun openConfirmScreen(dataResponse: Order) {
        intent.putExtra("EXTRA_ORDER", dataResponse)
        setResult(Activity.RESULT_OK, intent)
        finish()
//        RoundedOkDialog(getString(R.string.check_quality_add_container_turn_off_iot)).setRoundedDialogCallback(
//            object : RoundedDialog.RoundedDialogCallback {
//                override fun onFirstButtonClicked(selectedValue: Any?) {
//                    if (!viewModel.sensorConnected) {
//                        finish()
//                    }
//                }
//
//                override fun onSecondButtonClicked(selectedValue: Any?) {
//                }
//            }).show(supportFragmentManager, "")
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
}
