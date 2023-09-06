package com.zlatan.uv_today_android.viewModels
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zlatan.uv_today_android.models.dataModel.Index
import com.zlatan.uv_today_android.services.LocationService
import com.zlatan.uv_today_android.services.LocationServiceDelegate
import com.zlatan.uv_today_android.services.UVService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val throwable: Throwable? = null) : Resource<T>()
}

@HiltViewModel
class UVViewModel @Inject constructor(
    private val locationService: LocationService,
    private val uvService: UVService
): ViewModel(), LocationServiceDelegate {
    private val _index = MutableLiveData<Resource<Index>>()
    private val _showLoading = MutableLiveData<Boolean>()
    private val _city = MutableLiveData<Resource<String>>()
    val index: LiveData<Resource<Index>>
        get() = _index
    val showLoading: LiveData<Boolean>
        get() = _showLoading
    val city: LiveData<Resource<String>>
        get() = _city

    private var location: Location? = null

    fun attach() {
        // Do without delegate
        this.locationService.setDelegate(this)
    }

    fun searchLocation() {
        _showLoading.value = true
        this.locationService.retrieveLocation()
    }

    private fun getUVIndex() {
        val location = this.location ?: return

        viewModelScope.launch {
            try {
                val index = uvService.getUVIndex(location)
                _index.value = Resource.Success(index)
            } catch (e: Exception) {
                _index.value = Resource.Failure(e)
            }

            _showLoading.value = false
        }
    }

    override fun didUpdateLocation(location: Location, city: String) {
        this.location = location
        this._city.value = Resource.Success(city)
        Log.i("uv-today", "City : $city - ${location.latitude}:${location.longitude}")

        this.getUVIndex()
    }

    override fun didFailUpdateLocation() {
        _showLoading.value = false
        _city.value = Resource.Failure()
    }
}