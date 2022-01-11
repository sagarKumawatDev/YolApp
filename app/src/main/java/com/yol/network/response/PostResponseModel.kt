package com.yol.network.response

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class PostResponseModel {
    @SerializedName("records")
    @Expose
    var records: List<Record> = ArrayList()

    @SerializedName("result")
    @Expose
    var result: Int? = null

    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    inner class Record {
        @SerializedName("commentCount")
        @Expose
        var commentCount: Int? = null

        @SerializedName("likeCount")
        @Expose
        var likeCount: Int? = null

        @SerializedName("likes")
        @Expose
        var likes: List<Likes> = ArrayList()

        @SerializedName("media")
        @Expose
        var media: List<String>? = null

        @SerializedName("comments")
        @Expose
        var comments: List<Any>? = null

        @SerializedName("textContent")
        @Expose
        var textContent: String? = null

        @SerializedName("category")
        @Expose
        var category: String? = null

        @SerializedName("postInZone")
        @Expose
        var postInZone: Boolean? = null

        @SerializedName("public")
        @Expose
        var public: Boolean? = null

        @SerializedName("createdBy")
        @Expose
        var createdBy: CreatedBy? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null
    }

    inner class CreatedBy {
        @SerializedName("profilePicture")
        @Expose
        var profilePicture: String? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null
    }

    class Likes {
        @SerializedName("profilePicture")
        @Expose
        var profilePicture: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null
    }
}