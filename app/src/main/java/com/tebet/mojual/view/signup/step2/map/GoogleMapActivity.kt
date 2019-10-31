package com.tebet.mojual.view.signup.step2.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import co.common.view.dialog.RoundedCancelOkDialog
import co.common.view.dialog.RoundedDialog
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.Places.createClient
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tebet.mojual.R
import com.tebet.mojual.common.constant.ConfigEnv
import com.tebet.mojual.data.models.Address
import com.tebet.mojual.databinding.ActivitySignUpGoogleMapBinding
import com.tebet.mojual.view.base.BaseActivity
import com.tebet.mojual.view.qualitycontainer.QualityAddContainer
import kotlinx.android.synthetic.main.activity_sign_up_google_map.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber


class GoogleMapActivity : BaseActivity<ActivitySignUpGoogleMapBinding, GoogleMapViewModel>(), GoogleMapNavigator {
    private val INIT_PLACE = 0
    private val MOVE_CAMERA = 1
    private val CAMERA_IDLE = 2
    private var cameraState = INIT_PLACE
    private var AUTOCOMPLETE_REQUEST_CODE = 1000

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: GoogleMapViewModel
        get() = ViewModelProviders.of(this, factory).get(GoogleMapViewModel::class.java)

    override val contentLayoutId: Int
        get() = R.layout.activity_sign_up_google_map

    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var address: Address = Address()
//    private val defaultLocation = LatLng(21.035732, 105.8476363)

    private val perms = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var gpsDialog: RoundedDialog? = null

    override fun onCreateBase(savedInstanceState: Bundle?, layoutId: Int) {
        viewModel.navigator = this
        title = getString(R.string.registration_step2_google_map_title)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initMap()
        Places.initialize(applicationContext, ConfigEnv.googleApiKey)
        address = try {
            intent?.getSerializableExtra("LOCATION") as Address
        } catch (e: Exception) {
        } as Address

        var addressString = ""
        addressString += (if (!addressString.endsWith(" ")) " " else "") + (address.address ?: "")
        addressString += (if (!addressString.endsWith(" ")) " " else "") + (address.city ?: "")
        addressString += (if (!addressString.endsWith(" ")) " " else "") + (address.kecamatan ?: "")
        addressString += (if (!addressString.endsWith(" ")) " " else "") + (address.kelurahan ?: "")
        when {
            addressString.isNotEmpty() -> {
                val coder = Geocoder(this)
                try {
                    coder.getFromLocationName(addressString, 5).firstOrNull()?.let {
                        viewModel.selectedLocation = LatLng(it.latitude, it.longitude)
                        viewModel.getAddress(LatLng(it.latitude, it.longitude))
                        requestLocationAndInitMap()
                    }
                } catch (ex: Exception) {
                    Timber.e(ex.message)
                }
            }
            else -> requestLocationAndInitMap()
        }
        btnSelectLocation.setOnClickListener {
            viewModel.selectedLocation = googleMap?.cameraPosition?.target
            viewModel.getAddress(googleMap?.cameraPosition?.target) {
                finish()
            }
        }
        viewModel.loadData()
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { it ->
            googleMap = it
            viewModel.selectedLocation?.let { it1 -> moveMapTo(it1) }
            it.setOnCameraMoveStartedListener {
                Handler().postDelayed({ cameraState = MOVE_CAMERA }, 3000)
            }
            it.setOnCameraIdleListener {
                if (cameraState == MOVE_CAMERA) {
                    viewModel.selectedLocation = it.cameraPosition.target
                    viewModel.getAddress(viewModel.selectedLocation)
                    cameraState = CAMERA_IDLE
                    Handler().removeCallbacksAndMessages(null)
                }
            }
        }
    }

    private fun moveMapTo(location: LatLng) {
        val cu = CameraUpdateFactory.newLatLngZoom(location, 16f)
        googleMap?.animateCamera(cu)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun getUserLocation(callback: (() -> Unit)? = null) {
        if (!EasyPermissions.hasPermissions(this, *perms)) return
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                fusedLocationClient.removeLocationUpdates(this)
                locationResult?.locations?.firstOrNull()?.let {
                    if (viewModel.selectedLocation == null) {
                        viewModel.selectedLocation = LatLng(it.latitude, it.longitude)
                    }
                }
                callback?.let { it() }
            }
        }, null)
    }

    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { location ->
            getUserLocation {
                if (viewModel.selectedLocation == null) location.result?.let {
                    viewModel.selectedLocation = LatLng(it.latitude, it.longitude)
                }
                viewModel.selectedLocation?.let { moveMapTo(it) }
            }
        }
    }

    override fun finish() {
        viewModel.selectedLocation?.let {
            address.latitude = it.latitude
            address.longitude = it.longitude
            address.mapLocation = viewModel.selectedAddress.get()
            intent.putExtra(
                "LOCATION",
                address
            )
            setResult(Activity.RESULT_OK, intent)

        }
        super.finish()
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(QualityAddContainer.RC_CAMERA_AND_LOCATION)
    fun requestLocationAndInitMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(this, *perms)) {
                val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps()
                } else {
                    getLastLocation()
                }
            } else {
                // Do not have permissions, request them now
                EasyPermissions.requestPermissions(this, getString(R.string.check_quality_add_container_permission_warning),
                    QualityAddContainer.RC_CAMERA_AND_LOCATION, *perms)
            }
        } else {
            getLastLocation()
        }
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
                            QualityAddContainer.GPS_ENABLE
                        )
                    }
                })
        }
        gpsDialog?.show(supportFragmentManager, "")
    }


    override fun showAddressSearch() {
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
// Create a new Places client instance.
//            val placesClient = createClient(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            QualityAddContainer.GPS_ENABLE -> requestLocationAndInitMap()
            AUTOCOMPLETE_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                        viewModel.selectedAddress.set(place?.address)
                        viewModel.selectedLocation = place?.latLng
                        // do query with address
                        moveMapTo(LatLng(place?.latLng?.latitude ?: 0.0, place?.latLng?.longitude?: 0.0))
                    }
                    RESULT_ERROR -> {
                        val status = data?.let { Autocomplete.getStatusFromIntent(it) }
                        Toast.makeText(this, "Error: " + status?.statusMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                    RESULT_CANCELED -> {
                        // The user canceled the operation.
                    }
                }
            }
        }
    }
}
