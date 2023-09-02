package com.zlatan.uv_today_android.services

import android.location.Location
import com.zlatan.uv_today_android.BuildConfig
import com.zlatan.uv_today_android.models.dataModel.Index
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.math.roundToInt


interface UVService {
    suspend fun getUVIndex(location: Location): Index
}

class UVServiceImpl @Inject constructor(
    private val retrofit: Retrofit
): UVService {
    override suspend fun getUVIndex(location: Location): Index {
        val service = retrofit.create(GetUVApi::class.java)

        return service.getUVIndex(BuildConfig.OpenWeatherApiKey, location.latitude, location.longitude).value.roundToInt()
    }
}