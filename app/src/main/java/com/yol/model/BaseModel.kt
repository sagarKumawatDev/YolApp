package com.yol.model

import com.google.gson.annotations.SerializedName

class BaseModel<T>(
    @field:SerializedName("success")
    var success: Boolean,

    @field:SerializedName("message") var message: String,

    @field:SerializedName("status")
    var status: Int,

    @field:SerializedName("data")
    var data: T
)