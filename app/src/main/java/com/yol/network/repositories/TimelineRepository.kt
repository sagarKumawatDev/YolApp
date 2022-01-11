package com.yol.network.repositories

import com.yol.network.RestApiInterface
import com.yol.network.request.CreateEventRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class TimelineRepository(
    private val restApiInterface: RestApiInterface
) : BaseRepository(restApiInterface) {

    suspend fun createPost(params: Map<String, RequestBody>,filePart: MultipartBody.Part?) =
        safeApiCall { restApiInterface.createPost(params = params, filePart= filePart) }

    suspend fun getPosts(page: Int) = restApiInterface.getPosts(page = page)

    suspend fun likePost(postId: String) =safeApiCall { restApiInterface.likePost(postId = postId) }

    suspend fun unLikePost(postId: String) =safeApiCall { restApiInterface.unLikePost(postId = postId) }
}