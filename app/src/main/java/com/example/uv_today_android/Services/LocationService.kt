package com.example.uv_today_android.Services

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient

interface LocationServiceDelegate {
    fun didUpdateLocation(latitude: Double, longitude: Double, city: String)
    fun didFailUpdateLocation()
}

interface LocationService {
    fun setDelegate(delegate: LocationServiceDelegate)
    fun retriveLocation()
}

class LocationServiceImpl(
    private val locationClient: FusedLocationProviderClient,
    private val activity: Activity
): LocationService {

    private var delegate: LocationServiceDelegate? = null

    override fun setDelegate(delegate: LocationServiceDelegate) {
        this.delegate = delegate
    }

    override fun retriveLocation() {
        if (!this.checkPermissions()) {
            ActivityCompat.requestPermissions(this.activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            this.locationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        this.delegate?.didUpdateLocation(it.latitude, it.longitude, "City to update")
                        Log.i("uv-today", "Location is : ${it.latitude}, ${it.longitude}")
                    }
                } else {
                    this.delegate?.didFailUpdateLocation()
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this.activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}