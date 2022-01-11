package com.yol.model

import com.google.gson.annotations.SerializedName

class BaseModelPagination<T>(
    @field:SerializedName("success") var success: Boolean,
    @field:SerializedName(
        "message"
    ) var message: String? = "",
    @field:SerializedName("total") var total: Int,
    @field:SerializedName("limit") var limit: Int,
    @field:SerializedName("totalPages") var totalPages: Int,
    @field:SerializedName("page") var page: Int,
    @field:SerializedName("data") var data: T
)