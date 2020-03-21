package com.zlatan.uv_today_android.Services

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.IOException
import java.util.*

interface LocationServiceDelegate {
    fun didUpdateLocation(location: Location, city: String)
    fun didFailUpdateLocation()
}

interface LocationService {
    fun setDelegate(delegate: LocationServiceDelegate)
    fun retrieveLocation()
}

class LocationServiceImpl(
    private val locationClient: FusedLocationProviderClient,
    private val activity: Activity
): LocationService {

    private var delegate: LocationServiceDelegate? = null

    override fun setDelegate(delegate: LocationServiceDelegate) {
        this.delegate = delegate
    }

    override fun retrieveLocation() {
        if (!this.checkPermissions()) {
            ActivityCompat.requestPermissions(this.activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            this.locationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        this.delegate?.didUpdateLocation(it, this.findCityFromLocation(it))
                    }
                } else {
                    this.delegate?.didFailUpdateLocation()
                }
            }
        }
    }

    private fun findCityFromLocation(location: Location): String {
        val geocoder = Geocoder(this.activity.applicationContext, Locale.getDefault())
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (ioException: IOException) {
            return "Unknown"
        }

        if (addresses.isNotEmpty()) {
            return addresses.first().locality ?: "Unknown"
        }

        return "Unknown"
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this.activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}