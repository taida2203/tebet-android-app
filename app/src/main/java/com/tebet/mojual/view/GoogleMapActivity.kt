package com.tebet.mojual.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tebet.mojual.R
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class GoogleMapActivity : FragmentActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val defaultLocation = LatLng(21.035732, 105.8476363)

    private val perms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_google_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initPermission()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    lastLocation = location
                }
                stopLocationUpdate()
            }
        }
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
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, 101, perms[1])
                .setRationale(R.string.package_restrict_permission)
                .setPositiveButtonText(R.string.general_button_ok)
                .setNegativeButtonText(R.string.general_button_cancel)
                .build()
        )

        if (EasyPermissions.hasPermissions(this, perms[0]) && EasyPermissions.hasPermissions(this, perms[1])) {
            getUserLocation()
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { it ->
            googleMap = it
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
            } else {
                val cu = CameraUpdateFactory.newLatLngZoom(
                    defaultLocation
                    , 16f
                )
                val mark = googleMap.addMarker(
                    MarkerOptions().position(
                        defaultLocation
                    ).icon(BitmapDescriptorFactory.defaultMarker())
                )
                mark.showInfoWindow()
                googleMap.animateCamera(cu)
            }
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null)
                    lastLocation = location
                else {
                    getUserLocation()
                }
            }
        }
    }

    private fun getUserLocation() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            initPermission()
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
//        }
    }


}
