package com.yol.network

import com.google.gson.annotations.SerializedName

data class CreateEvent(
    @SerializedName("location") val location : Location,
    @SerializedName("eventName") val eventName : String,
    @SerializedName("eventDetail") val eventDetail : String,
    @SerializedName("eventType") val eventType : String,
    @SerializedName("likeCount") val likeCount : Int,
    @SerializedName("response") val response : List<String>,
    @SerializedName("friends") val friends : List<String>,
    @SerializedName("reports") val reports : List<String>,
    @SerializedName("_id") val _id : String,
    @SerializedName("public") val public : Boolean,
    @SerializedName("createdBy") val createdBy : String,
    @SerializedName("startDateTime") val startDateTime : String,
    @SerializedName("endDateTime") val endDateTime : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("distance") val distance : Int,
    @SerializedName("maxAge") val maxAge : Int,
    @SerializedName("minAge") val minAge : Int,
    @SerializedName("completeAddress") val completeAddress : String,
    @SerializedName("city") val city : String,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String,
    @SerializedName("__v") val __v : Int
)

data class Location (

    @SerializedName("type") val type : String,
    @SerializedName("coordinates") val coordinates : List<Double>
)