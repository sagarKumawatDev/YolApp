package com.yol.model

import com.google.gson.annotations.SerializedName

data class MindMapModel(
    @SerializedName("totalMap") val totalMap : Int,
    @SerializedName("totalShare") val totalShare : Int,
    @SerializedName("mindmap") val mindmap : ArrayList<Mindmap>,
    @SerializedName("mindshare") val mindshare : ArrayList<Mindshare>
)
data class Mindmap (
    @SerializedName("count") val count : Int,
    @SerializedName("lastDate") val lastDate : String? = "",
    @SerializedName("name") val name : String,
    @SerializedName("todayDate") val todayDate : String,
    @SerializedName("days") val days : Int
)

data class Mindshare (
    @SerializedName("count") val count : Int,
    @SerializedName("lastDate") val lastDate : String,
    @SerializedName("name") val name : String,
    @SerializedName("todayDate") val todayDate : String,
    @SerializedName("days") val days : Int
)