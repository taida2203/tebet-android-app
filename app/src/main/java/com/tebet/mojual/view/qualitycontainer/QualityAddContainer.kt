package com.tebet.mojual.view.qualitycontainer

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.*
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
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.tebet.mojual.R
import com.tebet.mojual.common.services.DigitalFootPrintServices
import com.tebet.mojual.common.util.Sensor
import com.tebet.mojual.data.models.Order
import com.tebet.mojual.databinding.ActivityQualityAddContainerBinding
import com.tebet.mojual.databinding.ItemHomeIconBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.help.QualityHelp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    private var previousConnectedWifi: String? = null

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
                baseBinding?.topRightHolder,
                true
            )
        intent.getSerializableExtra("EXTRA_ORDER")?.let {
            viewModel.order.set(it as Order)
            if (!viewModel.order.get()?.orderCode.isNullOrBlank()) title = String.format(getString(R.string.check_quality_add_container_order), viewModel.order.get()?.orderCode)
        }
        viewModel.loadData()

        val react = ReactiveNetwork
            .observeNetworkConnectivity(this)
//            .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
//            .filter(ConnectivityPredicate.hasType(ConnectivityManager.TYPE_WIFI))
            .concatMap {
                var nextOBS = Observable.just(true)
                if (it.state() == NetworkInfo.State.CONNECTED) {
                    if (it.extraInfo() != previousConnectedWifi) {
                        viewModel.sensorManager.get()?.refreshStatus()?.let { sensorRefreshObs ->
                            nextOBS = sensorRefreshObs
                        }
                        Timber.e("DOLPHIN" + it.typeName())
                    }
                    previousConnectedWifi = it.extraInfo()
                } else if (it.state() == NetworkInfo.State.DISCONNECTED) {
                    if (previousConnectedWifi?.contains(Sensor.sensorSSID) == true) {
                        viewModel.sensorManager.get()?.refreshStatus()?.let { sensorRefreshObs ->
                            nextOBS = sensorRefreshObs
                        }
                    }
                }
                nextOBS
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {})
        viewModel.compositeDisposable.add(react)
    }

    private fun buildAlertMessageNoGps() {
        if (gpsDialog == null) {
            gpsDialog = RoundedCancelOkDialog(getString(R.string.check_quality_add_container_gps_warning)).setRoundedDialogCallback(
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
            if (EasyPermissions.hasPermissions(this, *arrayOf(ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE, CHANGE_WIFI_STATE))) {
                val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps()
                } else {
                    viewModel.connectIOT()
                }
            } else {
                // Do not have permissions, request them now
                EasyPermissions.requestPermissions(this, getString(R.string.check_quality_add_container_permission_warning), RC_CAMERA_AND_LOCATION, *arrayOf(ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE, ACCESS_NETWORK_STATE, CHANGE_WIFI_STATE))
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
            WIFI_MANUAL -> viewModel.refreshIOTStatus()
        }
    }

    override fun openQualityHelpScreen() {
        startActivity(Intent(this, QualityHelp::class.java))
    }

    private var mConnectivityManager: ConnectivityManager? = null
    private var mNetworkCallback: ConnectivityManager.NetworkCallback? = null
    //...

    // https://stackoverflow.com/questions/36653126/send-request-over-wifi-without-connection-even-if-mobile-data-is-on-with-conn/51469732#51469732
    /**
     * This method sets a network callback that is listening for network changes and once is
     * connected to the desired WiFi network with the given SSID it will bind to that network.
     *
     * Note: requires android.permission.INTERNET and android.permission.CHANGE_NETWORK_STATE in
     * the manifest.
     *
     * @param ssid The name of the WiFi network you want to route your requests
     */
    override fun routeNetworkRequestsThroughWifi(ssid: String): Observable<Boolean> {
         return Observable.fromCallable<Boolean> {
             mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

             // ensure prior network callback is invalidated
             unregisterNetworkCallback(mNetworkCallback)

             // new NetworkRequest with WiFi transport type
             val request = NetworkRequest.Builder()
                 .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                 .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                 .build()

             // network callback to listen for network changes
             mNetworkCallback = object : ConnectivityManager.NetworkCallback() {

                 // on new network ready to use
                 override fun onAvailable(network: Network) {
                     if (viewModel.sensorManager.get()?.getIOTNetworkId() ?: 0 > 0) {
                         releaseNetworkRoute().subscribe(
                             { createNetworkRoute(network) },
                             { createNetworkRoute(network) })
                     } else {
                         releaseNetworkRoute().subscribe({}, {})
                     }
                 }
             }
             when {
                 // https://stackoverflow.com/questions/54306682/connectivitymanager-networkcallback-not-working-as-expected
                 Build.VERSION.SDK_INT < 24 -> mConnectivityManager?.registerNetworkCallback(request, mNetworkCallback)
             }
             mConnectivityManager?.requestNetwork(request, mNetworkCallback)
             true
         }
    }

    private fun unregisterNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback?) {
        if (networkCallback != null) {
            try {
                mConnectivityManager?.unregisterNetworkCallback(networkCallback)

            } catch (ignore: Exception) {
            } finally {
                mNetworkCallback = null
            }
        }
    }

    private fun createNetworkRoute(network: Network): Boolean? {
        return when {
            // 23 = Marshmallow
            Build.VERSION.SDK_INT >= 23 -> mConnectivityManager?.bindProcessToNetwork(network) ?: false
            else -> ConnectivityManager.setProcessDefaultNetwork(network)
        }
    }

    override fun releaseNetworkRoute(): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            val processBoundToNetwork = when {
                // 23 = Marshmallow
                Build.VERSION.SDK_INT >= 23 -> mConnectivityManager?.bindProcessToNetwork(null) ?: false
                else -> ConnectivityManager.setProcessDefaultNetwork(null)
            }
            processBoundToNetwork
        }
    }
}
