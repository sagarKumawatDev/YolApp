package com.yol.network.request

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class RegisterReq (
    @SerializedName("name")
    @Expose
    var name: String? = "",

    @SerializedName("mobile")
    @Expose
    var mobile: String? = "",

    @SerializedName("email")
    @Expose
    var email: String? = "",

    @SerializedName("password")
    @Expose
    var password: String? = "",

    @SerializedName("gender")
    @Expose
    var gender: String? = "",

    @SerializedName("college")
    @Expose
    var college: String? = "",

    @SerializedName("stream")
    @Expose
    var stream: String? = "",

    @SerializedName("dob")
    @Expose
    var dob: String? = "",

    @SerializedName("country")
    @Expose
    var country: String? = "",

    @SerializedName("pincode")
    @Expose
    var pincode: String? = "",

    @SerializedName("platform")
    @Expose
    var platform: String? = "",

    @SerializedName("registration_type")
    @Expose
    var registration_type: String = ""
)