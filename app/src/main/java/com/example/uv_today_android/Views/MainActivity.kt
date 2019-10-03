package com.example.uv_today_android.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uv_today_android.Presenters.UVView
import com.example.uv_today_android.R

class MainActivity : AppCompatActivity(), UVView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setupUI()
    }

    private fun setupUI() {
        this.supportActionBar?.hide()
    }

    override fun onShowLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
