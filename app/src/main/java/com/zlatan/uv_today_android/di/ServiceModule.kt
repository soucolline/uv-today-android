package com.zlatan.uv_today_android.di

import com.google.android.gms.location.LocationServices
import com.zlatan.uv_today_android.services.LocationService
import com.zlatan.uv_today_android.services.LocationServiceImpl
import com.zlatan.uv_today_android.services.UVService
import com.zlatan.uv_today_android.services.UVServiceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val serviceModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
    single<UVService> { UVServiceImpl() }
    single<LocationService> { LocationServiceImpl(get(), androidApplication()) }
}