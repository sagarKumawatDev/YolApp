package com.yol.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("status")
    @Expose
    var status: Boolean = false,
    @SerializedName("Message")
    @Expose
    var message: String = "",
    @SerializedName("Name")
    @Expose
    var Name: String = "",
)