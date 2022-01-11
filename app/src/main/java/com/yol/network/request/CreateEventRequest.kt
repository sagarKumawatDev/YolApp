package com.yol.network.request

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class CreateEventRequest (
    @SerializedName("eventName")
    @Expose
    var eventName: String? = null,

    @SerializedName("eventDetail")
    @Expose
    var eventDetail: String? = null,

    @SerializedName("eventType")
    @Expose
    var eventType: String? = null,

    @SerializedName("category")
    @Expose
    var category: String? = null,

    @SerializedName("startDateTime")
    @Expose
    var startDateTime: String? = null,

    @SerializedName("endDateTime")
    @Expose
    var endDateTime: String? = null,

    @SerializedName("gender")
    @Expose
    var gender: String? = null,

    @SerializedName("public")
    @Expose
    var public: Boolean? = null,

    @SerializedName("distance")
    @Expose
    var distance: Int? = null,

    @SerializedName("maxAge")
    @Expose
    var maxAge: Int? = null,

    @SerializedName("minAge")
    @Expose
    var minAge: Int? = null,

    @SerializedName("completeAddress")
    @Expose
    var completeAddress: String? = null,

    @SerializedName("city")
    @Expose
    var city: String? = null,

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null,

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null
)