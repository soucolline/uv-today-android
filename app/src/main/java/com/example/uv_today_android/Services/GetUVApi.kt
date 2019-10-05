package com.example.uv_today_android.Services

import com.example.uv_today_android.Models.DataModel.ServiceModels.ForecastObjectResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetUVApi {
    @GET("/forecast/{api_key}/{latitude},{longitude}")
    fun getUVIndex(
        @Path(value = "api_key", encoded = true) api_key: String,
        @Path(value = "latitude", encoded = true) latitude: Double,
        @Path(value = "longitude", encoded = true) longitude: Double
    ): Call<ForecastObjectResponse>
}