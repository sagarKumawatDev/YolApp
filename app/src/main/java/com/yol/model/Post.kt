package com.yol.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("commentCount") val commentCount: Int,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("likes") val likes: List<String>,
    @SerializedName("media") val media: List<String>,
    @SerializedName("comments") val comments: List<String>,
    @SerializedName("reports") val reports: List<String>,
    @SerializedName("_id") val _id: String,
    @SerializedName("textContent") val textContent: String,
    @SerializedName("category") val category: String,
    @SerializedName("postInZone") val postInZone: Boolean,
    @SerializedName("public") val public: Boolean,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val __v: Int,
    @SerializedName("id") val id: String


)