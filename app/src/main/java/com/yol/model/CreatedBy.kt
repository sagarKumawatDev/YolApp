package com.yol.model

import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("createdDate") var createdDate:String = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("profilePicture") var profilePicture: String? = "",
    @SerializedName("userId") var userId: String = ""
)