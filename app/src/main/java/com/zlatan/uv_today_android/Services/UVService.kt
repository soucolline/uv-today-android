package com.zlatan.uv_today_android.Services

import android.location.Location
import android.util.Log
import com.zlatan.uv_today_android.BuildConfig
import com.zlatan.uv_today_android.Models.DataModel.Index
import com.zlatan.uv_today_android.Models.DataModel.ServiceModels.ForecastObjectResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface UVService {
    fun getUVIndex(location: Location, result: (Index) -> Unit, error: (String) -> Unit)
}

class UVServiceImpl: UVService {

    override fun getUVIndex(location: Location, result: (Index) -> Unit, error: (String) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.darksky.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GetUVApi::class.java)
        val call = service.getUVIndex(BuildConfig.DarkSkyApiKey, location.latitude, location.longitude)

        call.enqueue(object : Callback<ForecastObjectResponse> {
            override fun onFailure(call: Call<ForecastObjectResponse>, t: Throwable) {
                error("An error occured ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<ForecastObjectResponse>, response: Response<ForecastObjectResponse>) {
                response.body()?.let {
                    Log.i("uv-today", "Retrieved uv Index successfully with : ${it.currently.uvIndex}")
                    result(it.currently.uvIndex)
                }
            }
        })
    }

}