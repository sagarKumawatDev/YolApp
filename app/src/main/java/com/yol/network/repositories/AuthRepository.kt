package com.yol.network.repositories

import com.yol.di.SafeApiCall
import com.yol.model.BaseModel
import com.yol.model.ClaperStatus
import com.yol.network.RestApiInterface
import com.yol.network.request.LoginRequest
import com.yol.network.request.RegisterReq
import com.yol.network.request.RegisterRequest
import com.yol.network.request.SendOtpRequest
import com.yol.network.response.*
import okhttp3.RequestBody
import java.util.*

class AuthRepository(
    private val restApiInterface: RestApiInterface
): BaseRepository(restApiInterface) {
    suspend fun login(loginRequest: LoginRequest) =
       safeApiCall { restApiInterface.login(loginRequest = loginRequest) }

    suspend fun register(registerRequest: RegisterReq) =
        safeApiCall {  restApiInterface.register(registerRequest = registerRequest)}

    suspend fun getUserDetails(tokenizer: String) =
        safeApiCall{restApiInterface.getUserDetails(tokenizer)}



    suspend fun getInstitute() =
       safeApiCall { restApiInterface.getInstitute() }

    suspend fun getStream()=
        safeApiCall { restApiInterface.getStream() }

    suspend fun sendOtp(sendOtpRequest: SendOtpRequest): SuccessResponse =
        restApiInterface.sendOtp(mobileno = sendOtpRequest)

    suspend fun verifyOtp(sendOtpRequest: SendOtpRequest): SuccessResponse =
        restApiInterface.verifyOtp(sendOtpRequest = sendOtpRequest)

    suspend fun changePassword(sendOtpRequest: SendOtpRequest): SuccessResponse =
        restApiInterface.changePassword(sendOtpRequest = sendOtpRequest)


    suspend fun updateTribes(requestBody: RequestBody) =
        safeApiCall { restApiInterface.updateTribes(requestBody = requestBody) }
}