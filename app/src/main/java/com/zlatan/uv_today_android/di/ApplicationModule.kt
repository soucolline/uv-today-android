package com.zlatan.uv_today_android.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zlatan.uv_today_android.services.LocationService
import com.zlatan.uv_today_android.services.LocationServiceImpl
import com.zlatan.uv_today_android.services.UVService
import com.zlatan.uv_today_android.services.UVServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideUVService(retrofit: Retrofit): UVService = UVServiceImpl(retrofit)

    @Provides
    fun provideLocationService(
        locationClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationService {
        return LocationServiceImpl(locationClient, context)
    }

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}