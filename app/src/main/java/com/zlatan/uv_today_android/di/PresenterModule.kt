package com.zlatan.uv_today_android.di

import com.zlatan.uv_today_android.Presenters.UVPresenter
import com.zlatan.uv_today_android.Presenters.UVPresenterImpl
import org.koin.dsl.module

val presenterModule = module {
    factory<UVPresenter> { UVPresenterImpl(get(), get()) }
}