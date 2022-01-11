package com.yol.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationData(
    @SerializedName("cityName") var cityName: String = "",
    @SerializedName("completeAddress") var completeAddress: String = "",
    @SerializedName("latitude") var latitude: Double = 0.0,
    @SerializedName("longitude") var longitude: Double = 0.0
)