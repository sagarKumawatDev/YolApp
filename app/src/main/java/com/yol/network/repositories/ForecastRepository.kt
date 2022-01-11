package com.yol.network.repositories

import com.yol.network.ForecastModelRequest
import com.yol.network.RestApiInterface
import okhttp3.MultipartBody

class ForecastRepository(
    private val restApiInterface: RestApiInterface
) : BaseRepository(restApiInterface) {
    suspend fun saveForecast(file: MultipartBody.Part) =
        safeApiCall { restApiInterface.saveForecast(file = file) }

    suspend fun getForecast() =
        safeApiCall { restApiInterface.getForecast() }

    suspend fun putForecast(forecastModelRequest: ForecastModelRequest) =
        safeApiCall { restApiInterface.putForecast(forecastModelRequest = forecastModelRequest) }

    suspend fun getMindMap() =
        safeApiCall { restApiInterface.getMindMap() }
}