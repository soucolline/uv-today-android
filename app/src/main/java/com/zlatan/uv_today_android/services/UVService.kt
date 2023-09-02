package com.zlatan.uv_today_android.services

import android.location.Location
import com.zlatan.uv_today_android.BuildConfig
import com.zlatan.uv_today_android.models.dataModel.Index
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


interface UVService {
    suspend fun getUVIndex(location: Location): Index
}

class UVServiceImpl: UVService {
    override suspend fun getUVIndex(location: Location): Index {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(GetUVApi::class.java)

        return service.getUVIndex(BuildConfig.OpenWeatherApiKey, location.latitude, location.longitude).value.roundToInt()
    }
}