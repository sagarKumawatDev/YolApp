package com.yol.network

import com.google.gson.annotations.SerializedName

data  class ForecastModelRequest (
    @SerializedName("today")
    var today: String = "",

    @SerializedName("yesterday")
    var yesterday: String = ""
)
