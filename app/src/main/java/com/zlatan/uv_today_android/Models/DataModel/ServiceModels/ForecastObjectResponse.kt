package com.zlatan.uv_today_android.Models.DataModel.ServiceModels

import com.google.gson.annotations.SerializedName

data class ForecastObjectResponse(
    val lat: Double,
    val lon: Double,
    @SerializedName("date_iso")
    val dateIso: String,
    val date: Int,
    val value: Double
)
