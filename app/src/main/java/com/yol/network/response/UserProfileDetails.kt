package com.yol.network.response

import com.google.gson.annotations.SerializedName

class UserProfileDetails(
    @SerializedName("country_code") val country_code: Int,
    @SerializedName("country") val country: String,
    @SerializedName("tribes") var tribes: ArrayList<String>,
    @SerializedName("profilePicture") val profilePicture: String,
    @SerializedName("happiness") val happiness: Int,
    @SerializedName("ipaddress") val ipaddress: String,
    @SerializedName("role") val role: String,
    @SerializedName("mobile_verified") val mobile_verified: Boolean,
    @SerializedName("email_verified") val email_verified: Boolean,
    @SerializedName("loginAttempts") val loginAttempts: Int,
    @SerializedName("saved") val saved: List<String>,
    @SerializedName("story") val story: String,
    @SerializedName("website") val website: String,
    @SerializedName("followers") val followers: List<String>,
    @SerializedName("following") val following: List<String>,
    @SerializedName("notification") val notification: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("deleted_at") val deleted_at: String,
    @SerializedName("_id") val _id: String,
    @SerializedName("blockExpires") val blockExpires: String,
    @SerializedName("name") val name: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("college") val college: String,
    @SerializedName("stream") val stream: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("firebase_id") val firebase_id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("fcm_token") val fcm_token: List<Fcm_token>,
    @SerializedName("platform") val platform: String,
    @SerializedName("id") val id: String
)
data class Fcm_token (

       @SerializedName("_id") val _id : String,
       @SerializedName("fcm_token") val fcm_token : String,
       @SerializedName("device_type") val device_type : String
)