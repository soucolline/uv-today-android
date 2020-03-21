package com.zlatan.uv_today_android.Views

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bugsnag.android.Bugsnag
import com.zlatan.uv_today_android.BuildConfig
import com.zlatan.uv_today_android.Models.DataModel.Index
import com.zlatan.uv_today_android.Models.DataModel.getAssociatedColorFromContext
import com.zlatan.uv_today_android.Models.DataModel.getAssociatedDescriptionFromContext
import com.zlatan.uv_today_android.Presenters.UVPresenter
import com.zlatan.uv_today_android.Presenters.UVView
import com.zlatan.uv_today_android.R
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), UVView {

    private lateinit var backgroundView: ConstraintLayout
    private lateinit var cityTextView: TextView
    private lateinit var uvTextView: TextView
    private lateinit var uvDescription: TextView
    private lateinit var refreshButton: ImageView

    private val presenter: UVPresenter by inject()
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        this.setupBugsnag()
        this.setupUI()

        this.presenter.attach(this)

        this.searchLocation()

        this.refreshButton.setOnClickListener {
            this.presenter.searchLocation()
        }
    }

    private fun setupBugsnag() {
        Bugsnag.init(this.applicationContext, BuildConfig.BugsnagAPIKey)
    }

    private fun setupUI() {
        this.supportActionBar?.hide()

        this.backgroundView = this.findViewById(R.id.background_view)
        this.cityTextView = this.findViewById(R.id.city_textview)
        this.uvTextView = this.findViewById(R.id.uv_textview)
        this.uvDescription = this.findViewById(R.id.uv_description)
        this.refreshButton = this.findViewById(R.id.refresh_button)

        this.cityTextView.text = this.getString(R.string.city_label_default, "-")
        this.uvTextView.text = "-"
        this.uvDescription.text = ""
    }

    private fun searchLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.presenter.searchLocation()
        }
    }

    override fun onShowLoading() {
        this.dialog?.hide()
        this.dialog = ProgressDialog.show(
            this,
            this.getString(R.string.loading_title),
            this.getString(R.string.loading_description)
        )
    }

    override fun onHideLoading() {
        this.dialog?.hide()
    }

    override fun onUpdateLocationWithSuccess(cityName: String) {
        this.cityTextView.text = this.getString(R.string.city_label_default, cityName)
    }

    override fun onUpdateLocationWithError() {
        Toast.makeText(this.applicationContext, this.getString(R.string.could_not_retrieve_position), Toast.LENGTH_LONG).show()
    }

    override fun onReceiveSuccess(index: Index) {
        this.uvTextView.text = index.toString()
        this.uvDescription.text = index.getAssociatedDescriptionFromContext(this.applicationContext)
        this.animateBackgroundColorFromIndex(index)
    }

    override fun onReceiveError(error: String) {
        Toast.makeText(this.applicationContext, error, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == 1) {
            when {
                grantResults.isEmpty() -> Log.i("uv-today", "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> this.presenter.searchLocation()
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

        ObjectAnimator.ofObject(this.backgroundView, "backgroundColor", ArgbEvaluator(), colorFrom, colorTo)
            .setDuration(duration.toLong())
            .start()
    }
}