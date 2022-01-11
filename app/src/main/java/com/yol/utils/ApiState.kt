package com.yol.utils

import android.util.Log
import androidx.annotation.Nullable
import com.google.firebase.database.DatabaseError


class ApiState<Any> constructor(
    val status: Status, @param:Nullable @field:Nullable
    val data: Any?,
    val error: Throwable?,
    val errorDatabaseError: DatabaseError? = null,

    ) {

    fun loading(): ApiState<Any> {
        return ApiState(Status.LOADING, null, null, null)
    }

    fun success(data: Any): ApiState<Any> {
        Log.e("code==",data.toString())
        return ApiState(Status.SUCCESS, data, null,null)
    }

    fun error(error: Throwable): ApiState<Any> {
        Log.e("code==",error.message.toString())
        return ApiState(Status.ERROR, null, error,null)
    }
    fun errorDatabaseError(error: DatabaseError): ApiState<Any> {
        Log.e("code==",error.message.toString())
        return ApiState(Status.DatabaseError, null, null,error)
    }

    enum class Status {
        LOADING,
        SUCCESS,
        ERROR,
        DatabaseError
    }
}