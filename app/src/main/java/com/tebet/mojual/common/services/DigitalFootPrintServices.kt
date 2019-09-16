package com.tebet.mojual.common.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.LocationData
import com.tebet.mojual.data.models.request.ScanLocationRequest
import dagger.android.AndroidInjection
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import javax.inject.Inject

class DigitalFootPrintServices : IntentService(DigitalFootPrintServices::class.java.simpleName),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    companion object{
        const val ORDER_ID = "ORDER_ID"
        const val ORDER_CODE = "ORDER_CODE"
        fun newIntent(
            context: Context,
            orderId: Long? = null,
            orderCode: String? = null
        ): Intent {
            val mServiceIntent = Intent(context, DigitalFootPrintServices::class.java)
            mServiceIntent.putExtra(ORDER_ID, orderId)
            mServiceIntent.putExtra(ORDER_CODE, orderCode)
            return mServiceIntent
        }

    }

    private var orderId: Long? = null
    private var orderCode: String? = null

    var mFusedLocationClient: FusedLocationProviderClient? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            super.onLocationAvailability(locationAvailability)
            if (locationAvailability != null && !locationAvailability.isLocationAvailable) {
                startCollectData()
            }
        }

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            mFusedLocationClient?.removeLocationUpdates(this)

            var locationData: LocationData? = null
            if (locationResult != null && locationResult.locations.isNotEmpty()) {
                locationData = LocationData(locationResult.locations[0])
            }
//
            if (mGoogleApiClient != null && mGoogleApiClient?.isConnected == true) {
                mGoogleApiClient?.disconnect()
            }
            startCollectData(locationData)
        }
    }

    @Inject
    lateinit var dataManager: DataManager


    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)

        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            when {
                mGoogleApiClient != null && mFusedLocationClient != null -> requestLocationUpdates()
                else -> buildGoogleApiClient()
            }
        } else {
            startCollectData()
        }
    }

    private fun startCollectData() {
        startCollectData(null)
//
    }

    override fun onHandleIntent(intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                // https://www.fabric.io/masbro/android/apps/co.masbro.consumer/issues/5ac742c036c7b23527af337b?time=last-seven-days
                val CHANNEL_ID = "tebet_channel_2"
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "tebet", NotificationManager.IMPORTANCE_DEFAULT
                )
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                    channel
                )
                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build()
                startForeground(2, notification)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        if (intent != null) {
            orderId = intent.getLongExtra(ORDER_ID, 0)
            orderCode = intent.getStringExtra(ORDER_CODE)
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient?.requestLocationUpdates(
                createLocationRequest(),
                mLocationCallback,
                Looper.myLooper()
            )
                ?.addOnCompleteListener { }?.addOnFailureListener { }?.addOnSuccessListener { }
        }
    }

    @SuppressLint("HardwareIds")
    private fun startCollectData(locationData: LocationData?) {
        locationData?.let {
            val uuid = try {
                Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            } catch (e: Exception) {
                ""
            }
            dataManager.scanLocation(
                ScanLocationRequest(
                    latitude = it.mLatitude,
                    longitude = it.mLongitude,
                    deviceId = uuid,
                    orderId = orderId,
                    orderCode = orderCode
                )
            ).subscribe({}, {error-> Timber.e(error)})
        }
    }

    private fun createLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 35000
        mLocationRequest.fastestInterval = 30000
        return mLocationRequest
    }

    override fun onConnected(p0: Bundle?) {
        requestLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

}