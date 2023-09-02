package com.zlatan.uv_today_android.di

import com.zlatan.uv_today_android.viewModels.UVViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presenterModule = module {
    viewModel { UVViewModel(get(), get()) }
}