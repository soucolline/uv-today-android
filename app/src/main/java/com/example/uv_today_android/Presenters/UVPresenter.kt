package com.example.uv_today_android.Presenters

import com.example.uv_today_android.Services.LocationService

interface UVView {
    fun onShowLoading()
    fun onHideLoading()
}

interface LeaderboardPresenter {
    fun setView(view: UVView)
}

class LeaderboardPresenterImpl(
    private val locationService: LocationService
): LeaderboardPresenter {

    private var view: UVView? = null

    override fun setView(view: UVView) {
        this.view = view
    }



}