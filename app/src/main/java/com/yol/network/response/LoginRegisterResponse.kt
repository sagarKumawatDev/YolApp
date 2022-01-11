package com.yol.network.response

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class LoginRegisterResponse {

        @SerializedName("access_token")
        @Expose
        var accessToken: String? = null

        @SerializedName("refresh_token")
        @Expose
        var refreshToken: String? = null

}