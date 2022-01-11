package com.yol.ui.home

import com.google.gson.annotations.SerializedName

data class ForecastModel(
    @SerializedName("isInTime")
    val isInTime: Boolean = false,
    @SerializedName("isCleverForecast")
    val isCleverForecast: Int = 0,
    @SerializedName("currentDate")
    val currentDate: String,
    @SerializedName("currentTime")
    val currentTime: String,
    @SerializedName("forecast")
    val forecast: Forecast
){
    val isMedia = forecast.media?.isEmpty()?.not()
}

data class Forecast(
    @SerializedName("status")
    val status: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("media")
    val media: String? = "",
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("__v")
    val __v: Int,
    @SerializedName("id")
    val id: String,

    @SerializedName("today")
    val today: String,

    @SerializedName("yesterday")
    val yesterday: String
)