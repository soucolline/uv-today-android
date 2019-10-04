package com.example.uv_today_android.Views

import android.app.ProgressDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.uv_today_android.Presenters.UVPresenter
import com.example.uv_today_android.Presenters.UVPresenterImpl
import com.example.uv_today_android.Presenters.UVView
import com.example.uv_today_android.R
import com.example.uv_today_android.Services.LocationServiceImpl
import com.example.uv_today_android.Services.UVServiceImpl
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), UVView {

    private lateinit var cityTextView: TextView

    private lateinit var presenter: UVPresenter
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setupUI()

        this.presenter = UVPresenterImpl(LocationServiceImpl(LocationServices.getFusedLocationProviderClient(this), this), UVServiceImpl())
        this.presenter.setView(this)
    }

    private fun setupUI() {
        this.supportActionBar?.hide()

        this.cityTextView = this.findViewById(R.id.city_textview)
    }

    override fun onShowLoading() {
        this.dialog?.hide()
        this.dialog = ProgressDialog.show(this, "Loading", "Retrieving your position")
    }

    override fun onHideLoading() {
        this.dialog?.hide()
    }

    override fun onUpdateLocationWithSuccess(cityName: String) {
        this.cityTextView.text = "Ville : $cityName"
    }

    override fun onUpdateLocationWithError() {
        Toast.makeText(this.applicationContext, "Could not retrieve your position", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == 1) {
            when {
                grantResults.isEmpty() -> Log.i("uv-today", "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> this.presenter.searchLocation()
                else -> {
                    Toast.makeText(this.applicationContext, "The permission to use your location was not given", Toast.LENGTH_LONG).show()
                    Log.e("uv-today", "User refused location")
                }
            }
        }
    }
}
