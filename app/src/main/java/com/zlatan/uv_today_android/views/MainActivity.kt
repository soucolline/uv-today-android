package com.zlatan.uv_today_android.views

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bugsnag.android.Bugsnag
import com.zlatan.uv_today_android.BuildConfig
import com.zlatan.uv_today_android.R
import com.zlatan.uv_today_android.databinding.ActivityMainBinding
import com.zlatan.uv_today_android.models.dataModel.Index
import com.zlatan.uv_today_android.models.dataModel.getAssociatedColorFromContext
import com.zlatan.uv_today_android.models.dataModel.getAssociatedDescriptionFromContext
import com.zlatan.uv_today_android.viewModels.Resource
import com.zlatan.uv_today_android.viewModels.UVViewModel
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: UVViewModel by viewModels()
    private var dialog: AlertDialog? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        this.setupBinding()
        this.setupBugsnag()
        this.setupUI()

        viewModel.attach()

        this.searchLocation()

        this.binding.refreshButton.setOnClickListener {
            this.viewModel.searchLocation()
        }

        viewModel.index.observe(this) { state ->
            when (state) {
                is Resource.Failure -> onReceiveError(state.throwable?.localizedMessage ?: "")
                is Resource.Success -> {
                    onReceiveSuccess(state.data)
                }
            }
        }

        viewModel.showLoading.observe(this) { shouldShow ->
            if (shouldShow) onShowLoading() else onHideLoading()
        }

        viewModel.city.observe(this) { state ->
            when(state) {
                is Resource.Failure -> onUpdateLocationWithError()
                is Resource.Success -> onUpdateLocationWithSuccess(state.data)
            }
        }
    }

    private fun setupBugsnag() {
        Bugsnag.init(this.applicationContext, BuildConfig.BugsnagAPIKey)
    }

    private fun setupUI() {
        this.supportActionBar?.hide()

        this.binding.cityTextview.text = this.getString(R.string.city_label_default, "-")
        this.binding.uvTextview.text = "-"
        this.binding.uvDescription.text = ""
    }

    private fun setupBinding() {
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun searchLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.searchLocation()
        } else {
            val permissions = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )
            val requestCode = LOCATION_PERMISSION_CODE
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    private fun onShowLoading() {
        this.dialog?.hide()
        this.dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage(R.string.loading_description)
            .build()
            .apply { show() }
    }

    private fun onHideLoading() {
        this.dialog?.hide()
    }

    private fun onUpdateLocationWithSuccess(cityName: String) {
        this.binding.cityTextview.text = this.getString(R.string.city_label_default, cityName)
    }

    private fun onUpdateLocationWithError() {
        Toast.makeText(this.applicationContext, this.getString(R.string.could_not_retrieve_position), Toast.LENGTH_LONG).show()
    }

    private fun onReceiveSuccess(index: Index) {
        this.binding.uvTextview.text = index.toString()
        this.binding.uvDescription.text = index.getAssociatedDescriptionFromContext(this.applicationContext)
        this.animateBackgroundColorFromIndex(index)
    }

    private fun onReceiveError(error: String) {
        Toast.makeText(this.applicationContext, error, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            when {
                grantResults.isEmpty() -> Log.i("uv-today", "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> viewModel.searchLocation()
                else -> {
                    Toast.makeText(this.applicationContext, this.getString(R.string.position_not_allowed), Toast.LENGTH_LONG).show()
                    Log.e("uv-today", "User refused location")
                }
            }
        }
    }

    private fun animateBackgroundColorFromIndex(index: Index) {
        val colorFrom = ContextCompat.getColor(this.applicationContext, R.color.app_blue)
        val colorTo = index.getAssociatedColorFromContext(this.applicationContext)
        val duration = 1000

        ObjectAnimator.ofObject(this.binding.backgroundView, "backgroundColor", ArgbEvaluator(), colorFrom, colorTo)
            .setDuration(duration.toLong())
            .start()
    }

    companion object {
        const val LOCATION_PERMISSION_CODE = 123
    }
}
