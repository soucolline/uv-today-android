package com.zlatan.uv_today_android.Presenters
import android.location.Location
import android.util.Log
import com.zlatan.uv_today_android.Models.DataModel.Index
import com.zlatan.uv_today_android.Services.LocationService
import com.zlatan.uv_today_android.Services.LocationServiceDelegate
import com.zlatan.uv_today_android.Services.UVService

interface UVView {
    fun onShowLoading()
    fun onHideLoading()

    fun onUpdateLocationWithSuccess(cityName: String)
    fun onUpdateLocationWithError()

    fun onReceiveSuccess(index: Index)
    fun onReceiveError(error: String)
}

interface UVPresenter {
    fun attach(view: UVView)

    fun searchLocation()
    fun getUVIndex()
}

class UVPresenterImpl(
    private val locationService: LocationService,
    private val uvService: UVService
): UVPresenter, LocationServiceDelegate {

    private var view: UVView? = null
    private var location: Location? = null

    override fun attach(view: UVView) {
        this.view = view
        this.locationService.setDelegate(this)
    }

    override fun searchLocation() {
        this.view?.onShowLoading()
        this.locationService.retrieveLocation()
    }

    override fun getUVIndex() {
        val location = this.location ?: return

        this.uvService.getUVIndex(location, {
            this.view?.onHideLoading()
            this.view?.onReceiveSuccess(it)
        }, {
            this.view?.onHideLoading()
            this.view?.onReceiveError(it)
        })
    }

    override fun didUpdateLocation(location: Location, city: String) {
        this.location = location
        this.view?.onUpdateLocationWithSuccess(city)
        Log.i("uv-today", "City : $city - ${location.latitude}:${location.longitude}")

        this.getUVIndex()
    }

    override fun didFailUpdateLocation() {
        this.view?.onHideLoading()
        this.view?.onUpdateLocationWithError()
    }
}