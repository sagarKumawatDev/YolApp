package com.yol.network.repositories

import com.yol.di.SafeApiCall
import com.yol.network.RestApiInterface

abstract class BaseRepository(private val api: RestApiInterface) : SafeApiCall {

//    suspend fun logout() = safeApiCall {
//        api.logout()
//    }
}