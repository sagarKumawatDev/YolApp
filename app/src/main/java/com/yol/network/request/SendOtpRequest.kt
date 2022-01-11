package com.yol.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendOtpRequest(
    @SerializedName("mobileno")
    @Expose
    var mobileno: String = "",

    @SerializedName("otp")
    @Expose
    var otp: String = "",

    @SerializedName("password")
    @Expose
    var password: String = "",

)