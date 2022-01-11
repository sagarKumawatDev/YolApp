package com.yol.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.yol.di.Resource
import com.yol.model.MindMapModel
import com.yol.model.Mindmap
import com.yol.network.ForecastModelRequest
import com.yol.network.repositories.AuthRepository
import com.yol.network.repositories.ForecastRepository
import com.yol.network.request.RegisterRequest
import com.yol.ui.home.ForecastModel
import com.yol.utils.ApiState
import com.yol.utils.MyApplication
import com.yol.utils.handleApiError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class MainActivityViewModel(
    private val forecastRepository: ForecastRepository,
) : ViewModel() {


    var TOTAL_POST_COUNT = 0
    var AMBITION_COUNT = 0
    var COMMITMENTS_COUNT = 0
    var CONFESSION_COUNT = 0
    var CONSCIENCE_COUNT = 0
    var DILEMMAS_COUNT = 0
    var INHIBITIONS_COUNT = 0
    var DOCUMENT_COUNT = 0
    var FAMILY_COUNT = 0
    var FRIENDS_COUNT = 0
    var HEALTH_COUNT = 0
    var OUTINGS_COUNT = 0
    var WALLET_COUNT = 0

    var AMBITION_LAST_DATE = ""
    var COMMITMENTS_LAST_DATE = ""
    var CONFESSION_LAST_DATE = ""
    var CONSCIENCE_LAST_DATE = ""
    var DILEMMAS_LAST_DATE = ""
    var INHIBITIONS_LAST_DATE = ""
    var DOCUMENT_LAST_DATE = ""
    var FAMILY_LAST_DATE = ""
    var FRIENDS_LAST_DATE = ""
    var HEALTH_LAST_DATE = ""
    var OUTINGS_LAST_DATE = ""
    var WALLET_LAST_DATE = ""

    var clapperProgressBar = MutableLiveData<ApiState<Boolean>>()
    private val _clapperProgressBar: ApiState<Boolean> =
        ApiState(ApiState.Status.LOADING, null, null, null)


    var claperStatus = MutableLiveData<ApiState<ForecastModel>>()
    private val _claperStatus: ApiState<ForecastModel> =
        ApiState(ApiState.Status.LOADING, null, null)

    var map = MutableLiveData<ApiState<MindMapModel>>()
    private val _map: ApiState<MindMapModel> =
        ApiState(ApiState.Status.LOADING, null, null, null)

    var unloadedContentUrl = MutableLiveData<ApiState<String>>()
    private val _unloadedContentUrl: ApiState<String> =
        ApiState(ApiState.Status.LOADING, null, null, null)
    var selfieForTodayUrl: String = ""
    var isforecastForToday: Boolean = false
    var isclaperStatus: Int? = null
    var todayFocecast: String? = null
    var yesterdayFocecast: String? = null

    var userforecastSelfieForToday = MutableLiveData<ApiState<String>>()
    private val _userforecastSelfieForToday: ApiState<String> =
        ApiState(ApiState.Status.LOADING, null, null, null)

    //saveForecast
    fun saveForecast(file: MultipartBody.Part) {
        clapperProgressBar.value = _clapperProgressBar.loading()

        viewModelScope.launch {
            runCatching {
                forecastRepository.saveForecast(file = file)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                       Log.e("User -> ","${it.value.data}")
                        clapperProgressBar.value = _clapperProgressBar.success(true)
                        getForeCast()

                    }
                    is Resource.Failure -> {
                        Log.e("User -> ","${it.message}")
                        handleApiError(it)

                    }
                    else -> Unit
                }
            }.onFailure {
                println(it.message)
            }
        }
    }

    //UserDetails
    fun getForeCast() {
        claperStatus.value = _claperStatus.loading()
        viewModelScope.launch {
            runCatching {
                forecastRepository.getForecast()
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                       Log.e("User -> ","${it.value.data}")
                        claperStatus.value = _claperStatus.success(it.value.data)

                    }
                    is Resource.Failure -> {
                        Log.e("User -> ","${it.message}")
                        handleApiError(it)

                    }
                    else -> Unit
                }
            }.onFailure {
                println(it.message)
                claperStatus.value = _claperStatus.error(it)

            }
        }
    }

    fun putForecast(forecastModelRequest: ForecastModelRequest) {
        viewModelScope.launch {
            runCatching {
                forecastRepository.putForecast(forecastModelRequest = forecastModelRequest)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                       Log.e("User -> ","${it.value.data}")
                        getForeCast()
                    }
                    is Resource.Failure -> {
                       Log.e("User -> ","${it.message}")
                        handleApiError(it)

                    }
                    else -> Unit
                }
            }.onFailure {
                println(it.message)
                claperStatus.value = _claperStatus.error(it)

            }
        }
    }

    //UserDetails
    fun getMindMap() {
        map.value = _map.loading()
        viewModelScope.launch {
            runCatching {
                forecastRepository.getMindMap()
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        Log.e("User -> ","${it.value.data}")
                        map.value = _map.success(data = it.value.data)
                        TOTAL_POST_COUNT = it.value.data.totalMap

                    }
                    is Resource.Failure -> {
                        Log.e("User -> ","${it.message}")
                        map.value = _map.error(Throwable(""))

                        handleApiError(it)

                    }
                    else -> Unit
                }
            }.onFailure {
                println(it.message)
                claperStatus.value = _claperStatus.error(it)

            }
        }
    }

}