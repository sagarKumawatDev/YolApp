package com.yol.network.response

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.util.ArrayList

class EventResponseModel {
    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("totalPages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("page")
    @Expose
    val page: Int? = null

    @SerializedName("records")
    @Expose
    val records: List<Record> = ArrayList()

    inner class CreatedBy {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("country_code")
        @Expose
        var countryCode: Int? = null

        @SerializedName("country")
        @Expose
        var country: String? = null

        @SerializedName("tribes")
        @Expose
        var tribes: ArrayList<Any>? = null

        @SerializedName("profilePicture")
        @Expose
        var profilePicture: String? = null

        @SerializedName("happiness")
        @Expose
        var happiness: Any? = null

        @SerializedName("ipaddress")
        @Expose
        var ipaddress: String? = null

        @SerializedName("role")
        @Expose
        var role: String? = null

        @SerializedName("mobile_verified")
        @Expose
        var mobileVerified: Boolean? = null

        @SerializedName("email_verified")
        @Expose
        var emailVerified: Boolean? = null

        @SerializedName("loginAttempts")
        @Expose
        var loginAttempts: Int? = null

        @SerializedName("saved")
        @Expose
        var saved: ArrayList<Any>? = null

        @SerializedName("story")
        @Expose
        var story: String? = null

        @SerializedName("website")
        @Expose
        var website: String? = null

        @SerializedName("followers")
        @Expose
        var followers: ArrayList<Any>? = null

        @SerializedName("following")
        @Expose
        var following: ArrayList<Any>? = null

        @SerializedName("notification")
        @Expose
        var notification: Boolean? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null

        @SerializedName("blockExpires")
        @Expose
        var blockExpires: String? = null

        @SerializedName("fcm_token")
        @Expose
        var fcmToken: ArrayList<FcmToken>? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("mobile")
        @Expose
        var mobile: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("college")
        @Expose
        var college: String? = null

        @SerializedName("stream")
        @Expose
        var stream: String? = null

        @SerializedName("pincode")
        @Expose
        var pincode: String? = null

        @SerializedName("dob")
        @Expose
        var dob: String? = null

        @SerializedName("firebase_id")
        @Expose
        var firebaseId: String? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null

        @SerializedName("platform")
        @Expose
        var platform: String? = null
    }

    inner class FcmToken {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("fcm_token")
        @Expose
        var fcmToken: String? = null

        @SerializedName("device_type")
        @Expose
        var deviceType: String? = null
    }

    inner class Location {
        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("coordinates")
        @Expose
        var coordinates: ArrayList<Double>? = null
    }

    inner class MyResponse {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("event_id")
        @Expose
        var eventId: String? = null

        @SerializedName("user_id")
        @Expose
        var userId: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("response")
        @Expose
        var response: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null
    }

    inner class Record {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("location")
        @Expose
        var location: Location? = null

        @SerializedName("eventName")
        @Expose
        var eventName: String? = null

        @SerializedName("eventDetail")
        @Expose
        var eventDetail: String? = null

        @SerializedName("eventType")
        @Expose
        var eventType: String? = null

        @SerializedName("likeCount")
        @Expose
        var likeCount: Int? = null

        @SerializedName("response")
        @Expose
        var response: ArrayList<Response>? = null

        @SerializedName("friends")
        @Expose
        var friends: ArrayList<Any>? = null

        @SerializedName("reports")
        @Expose
        var reports: ArrayList<Any>? = null

        @SerializedName("public")
        @Expose
        var public: Boolean? = null

        @SerializedName("createdBy")
        @Expose
        var createdBy: CreatedBy? = null

        @SerializedName("startDateTime")
        @Expose
        var startDateTime: String? = null

        @SerializedName("endDateTime")
        @Expose
        var endDateTime: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("distance")
        @Expose
        var distance: Float? = null

        @SerializedName("maxAge")
        @Expose
        var maxAge: Int? = null

        @SerializedName("minAge")
        @Expose
        var minAge: Int? = null

        @SerializedName("completeAddress")
        @Expose
        var completeAddress: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null

        @SerializedName("arrayIndex")
        @Expose
        var arrayIndex: Int? = null

        @SerializedName("myResponse")
        @Expose
        var myResponse: MyResponse? = null
    }

    inner class Response {
        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("event_id")
        @Expose
        var eventId: String? = null

        @SerializedName("user_id")
        @Expose
        var userId: String? = null

        @SerializedName("__v")
        @Expose
        var v: Int? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("response")
        @Expose
        var response: String? = null

        @SerializedName("updatedAt")
        @Expose
        var updatedAt: String? = null
    }
}