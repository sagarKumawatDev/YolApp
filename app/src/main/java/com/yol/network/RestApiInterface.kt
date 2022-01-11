package com.yol.network


import com.yol.model.BaseModel
import com.yol.model.ClaperStatus
import com.yol.model.MindMapModel
import com.yol.model.Post
import com.yol.network.request.*
import com.yol.network.response.*
import com.yol.ui.home.ForecastModel
import okhttp3.MultipartBody
import retrofit2.http.*

import retrofit2.http.POST

import okhttp3.RequestBody

import retrofit2.http.Multipart


interface RestApiInterface {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): BaseModel<LoginRegisterResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterReq): BaseModel<LoginRegisterResponse>

    @POST("sendotp")
    suspend fun sendOtp(@Body mobileno: SendOtpRequest): SuccessResponse


    @POST("verifyotp")
    suspend fun verifyOtp(@Body sendOtpRequest: SendOtpRequest): SuccessResponse


    @POST("changepassword")
    suspend fun changePassword(@Body sendOtpRequest: SendOtpRequest): SuccessResponse


    @POST("getwebuser")
    suspend fun getWebUser(): ArrayList<RegisterRequest>

    @POST("getclaperstatus")
    suspend fun getclaperstatus(): ClaperStatus


    @GET("profile")
    suspend fun getUserDetails(@Header("authorization") token : String): BaseModel<UserProfileDetails>

    @POST("posts")
    @Multipart
    @JvmSuppressWildcards
    suspend fun createPost(
        @PartMap params: Map<String, RequestBody>,
        @Part filePart: MultipartBody.Part?
    ): BaseModel<Post>


    @POST("events")
    suspend fun createEvent(@Body createEventRequest: CreateEventRequest): BaseModel<CreateEvent>


    @POST("forecast")
    @Multipart
    suspend fun saveForecast(@Part file: MultipartBody.Part): BaseModel<ForecastModel>

    @GET("forecast")
    suspend fun getForecast(): BaseModel<ForecastModel>

    @PUT("forecast")
    suspend fun putForecast(@Body forecastModelRequest: ForecastModelRequest): BaseModel<ForecastModel>

    @GET("events")
    suspend fun getEvents(@Query("page") page:Int): BaseModel<EventResponseModel>

    @GET("mindmap")
    suspend fun getMindMap(): BaseModel<MindMapModel>

    @GET("posts")
    suspend fun getPosts(@Query("page") page:Int): BaseModel<PostResponseModel>

    @GET("institutes")
    suspend fun getInstitute(): BaseModel<ArrayList<Institute>>

    @GET("streams")
    suspend fun getStream(): BaseModel<ArrayList<Streams>>

    @PATCH("post/{postId}/like")
    suspend fun likePost(@Path("postId") postId:String): BaseModel<PostResponseModel.Record>


    @PATCH("post/{postId}/unlike")
    suspend fun unLikePost(@Path("postId") postId:String): BaseModel<PostResponseModel.Record>

    @PATCH("update-tribes")
    suspend fun updateTribes(@Body requestBody: RequestBody): BaseModel<UserProfileDetails>

}