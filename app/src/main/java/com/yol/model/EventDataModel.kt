package com.yol.model

import com.google.gson.annotations.SerializedName

data class EventDataModel(
    @SerializedName("commentsCount") var commentsCount: Int = 0,
    @SerializedName("createdBy") var createdBy: CreatedBy = CreatedBy(),
    @SerializedName("distance") var distance: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("endDateTime") var endDateTime: String = "",
    @SerializedName("eventId") var eventId: String = "",
    @SerializedName("eventName") var eventName: String = "",
    @SerializedName("eventType") var eventType: String = "",
    @SerializedName("friends") var friends: String = "",
    @SerializedName("gender") var gender: String = "",
    @SerializedName("likesCount") var likesCount: Int = 0,
    @SerializedName("location") var location: LocationData = LocationData(),
    @SerializedName("maxAge") var maxAge: Int = 0,
    @SerializedName("minAge") var minAge: Int = 0,
    @SerializedName("isPublic") var isPublic: Boolean = false,
    @SerializedName("startDateTime") var startDateTime: String = "",
    @SerializedName("timeStamp") var timeStamp: String = "",
    @SerializedName("Likes") var likes: ArrayList<CreatedBy> = ArrayList()

)