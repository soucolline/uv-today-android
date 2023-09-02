package com.zlatan.uv_today_android.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
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
    private val context: Context
) : LocationService {

    private var delegate: LocationServiceDelegate? = null

    override fun setDelegate(delegate: LocationServiceDelegate) {
        this.delegate = delegate
    }

    @SuppressLint("MissingPermission")
    override fun retrieveLocation() {
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

    private fun findCityFromLocation(location: Location): String {
        val geocoder = Geocoder(this.context, Locale.getDefault())
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.toList() ?: emptyList()
        } catch (ioException: IOException) {
            return "Unknown"
        }

        if (addresses.isNotEmpty()) {
            return addresses.first().locality ?: "Unknown"
        }

        return "Unknown"
    }
}