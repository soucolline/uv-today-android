package com.zlatan.uv_today_android.services

import com.zlatan.uv_today_android.models.dataModel.serviceModels.ForecastObjectResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GetUVApi {
    @GET("uvi")
    suspend fun getUVIndex(
        @Query(value = "appid", encoded = true) api_key: String,
        @Query(value = "lat", encoded = true) latitude: Double,
        @Query(value = "lon", encoded = true) longitude: Double
    ): ForecastObjectResponse
}