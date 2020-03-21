package com.zlatan.uv_today_android.di

import com.google.android.gms.location.LocationServices
import com.zlatan.uv_today_android.Services.LocationService
import com.zlatan.uv_today_android.Services.LocationServiceImpl
import com.zlatan.uv_today_android.Services.UVService
import com.zlatan.uv_today_android.Services.UVServiceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val serviceModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
    single<UVService> { UVServiceImpl() }
    single<LocationService> { LocationServiceImpl(get(), androidApplication()) }
}