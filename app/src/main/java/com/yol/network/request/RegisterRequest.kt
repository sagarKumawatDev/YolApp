package com.yol.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("DOB")
    @Expose
    var dob: String = "",

    @SerializedName("Gender")
    @Expose
    var gender: String = "",

    @SerializedName("Hapiness")
    @Expose
    var happiness: String = "0",

    @SerializedName("Name")
    @Expose
    var name: String? = "",

    @SerializedName("mobileno")
    @Expose
    var mobileno: String = "",

    @SerializedName("latitude")
    @Expose
    var latitude: String = "",

    @SerializedName("longitude")
    @Expose
    var longitude: String = "",

    @SerializedName("ipaddress")
    @Expose
    var ipaddress: String = "",

    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("password")
    @Expose
    var password: String = "",

    @SerializedName("Collage")
    @Expose
    var collage: String = "",

    @SerializedName("Stream")
    @Expose
    var stream: String = "",

    @SerializedName("Pincode")
    @Expose
    var pincode: String = "",

    @SerializedName("usertoken")
    @Expose
    var usertoken: String = "",

    @SerializedName("country")
    @Expose
    var country: String = "",

    @SerializedName("UserId")
    @Expose
    var userId: String = "",

    @SerializedName("ProfilePicture")
    @Expose
    var ProfilePicture: String? = "",

    @SerializedName("RegisterDate")
    @Expose
    var RegisterDate: String = "",

    @SerializedName("platform")
    @Expose
    var platform: String = "android",

    @SerializedName("registration_type")
    @Expose
    var registration_type: String = ""
    )