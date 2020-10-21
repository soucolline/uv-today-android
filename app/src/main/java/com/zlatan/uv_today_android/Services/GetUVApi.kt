package com.zlatan.uv_today_android.Services

import com.zlatan.uv_today_android.Models.DataModel.ServiceModels.ForecastObjectResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetUVApi {
    @GET("uvi")
    fun getUVIndex(
        @Query(value = "appid", encoded = true) api_key: String,
        @Query(value = "lat", encoded = true) latitude: Double,
        @Query(value = "lon", encoded = true) longitude: Double
    ): Call<ForecastObjectResponse>
}