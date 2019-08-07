package com.tebet.mojual.view.signup.step2.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tebet.mojual.BR
import com.tebet.mojual.R
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.databinding.ActivitySignUpGoogleMapBinding
import com.tebet.mojual.view.base.BaseActivity
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class GoogleMapActivity : BaseActivity<ActivitySignUpGoogleMapBinding, GoogleMapViewModel>(),
    EasyPermissions.PermissionCallbacks {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: GoogleMapViewModel
        get() = ViewModelProviders.of(this, factory).get(GoogleMapViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_google_map

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    var address: Address = Address()
//    private val defaultLocation = LatLng(21.035732, 105.8476363)

    private val perms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    private var lastLocation: Location? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = "Select location on Map"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    lastLocation = location
                }
                stopLocationUpdate()
            }
        }
        address = try {
            intent?.getSerializableExtra("LOCATION") as Address
        } catch (e: Exception) {
        } as Address
        initPermission()
        initMap()
    }

    private fun initPermission() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, 100, perms[0])
                .setRationale(R.string.package_restrict_permission)
                .setPositiveButtonText(R.string.general_button_ok)
                .setNegativeButtonText(R.string.general_button_cancel)
                .build()
        )
//        EasyPermissions.requestPermissions(
//            PermissionRequest.Builder(this, 101, perms[1])
//                .setRationale(R.string.package_restrict_permission)
//                .setPositiveButtonText(R.string.general_button_ok)
//                .setNegativeButtonText(R.string.general_button_cancel)
//                .build()
//        )

        if (EasyPermissions.hasPermissions(this, perms[0])) {
//            getUserLocation()
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { it ->
            googleMap = it
            googleMap.setOnMapClickListener { selectedLocation ->
                viewModel.selectedLocation = selectedLocation
                viewModel.getAddress(selectedLocation)
            }
            Handler().postDelayed({
                if (lastLocation != null) {
                    val cu = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lastLocation?.latitude ?: 0.0,
                            lastLocation?.longitude ?: 0.0
                        )
                        , 16f
                    )
                    val mark = googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                lastLocation?.latitude ?: 0.0,
                                lastLocation?.longitude ?: 0.0
                            )
                        ).icon(BitmapDescriptorFactory.defaultMarker())
                    )
                    mark.showInfoWindow()
                    googleMap.animateCamera(cu)
                }
            }, 1000)
        }
    }


    private fun stopLocationUpdate() {

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        }
    }

    private fun getUserLocation() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            initPermission()
        } else {
//            getUserLocation()
            getLastLocation()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null)
                lastLocation = location
        }
    }

    override fun finish() {
        viewModel.selectedLocation?.let {
            address.     latitude = it.latitude
            address.longitude = it.longitude
            address.localAddress = viewModel.selectedAddress
            intent.putExtra(
                "LOCATION",
                address
            )
            setResult(Activity.RESULT_OK, intent)

        }
        super.finish()
    }
}
