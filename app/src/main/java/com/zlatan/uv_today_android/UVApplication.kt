package com.zlatan.uv_today_android

import android.app.Application
import com.zlatan.uv_today_android.di.presenterModule
import com.zlatan.uv_today_android.di.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class UVApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@UVApplication)
            modules(serviceModule, presenterModule)
        }
    }

}