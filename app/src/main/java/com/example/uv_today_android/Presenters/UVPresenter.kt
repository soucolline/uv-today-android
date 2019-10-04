package com.example.uv_today_android.Presenters

import com.example.uv_today_android.Services.LocationService
import com.example.uv_today_android.Services.LocationServiceDelegate

interface UVView {
    fun onShowLoading()
    fun onHideLoading()

    fun onUpdateLocationWithSuccess(cityName: String)
    fun onUpdateLocationWithError()
}

interface UVPresenter {
    fun setView(view: UVView)

    fun getLocation()
}

class UVPresenterImpl(
    private val locationService: LocationService
): UVPresenter, LocationServiceDelegate {

    private var view: UVView? = null

    override fun setView(view: UVView) {
        this.view = view
        this.locationService.setDelegate(this)
        this.getLocation()
    }

    override fun getLocation() {
        this.view?.onShowLoading()
        this.locationService.retrieveLocation()
    }

    override fun didUpdateLocation(latitude: Double, longitude: Double, city: String) {
        this.view?.onHideLoading()
        this.view?.onUpdateLocationWithSuccess(city)
    }

    override fun didFailUpdateLocation() {
        this.view?.onHideLoading()
        this.view?.onUpdateLocationWithError()
    }

}