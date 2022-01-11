package com.yol.ui.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yol.utils.ApiState
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yol.di.Resource
import com.yol.network.repositories.TimelineRepository
import com.yol.network.repositories.pagination.PaginationPosts
import com.yol.network.response.PostResponseModel
import com.yol.utils.ProgressDialog
import com.yol.utils.handleApiError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.collections.HashMap

class TimeLineViewModel(
    private val timelineRepository: TimelineRepository
) : ViewModel() {
    var post = MutableLiveData<ApiState<Boolean>>()
    private val _post: ApiState<Boolean> =
        ApiState(ApiState.Status.LOADING, null, null, null)

    var likeUnLikePost = MutableLiveData<ApiState<PostResponseModel.Record>>()
    val _likeUnLikePost: ApiState<PostResponseModel.Record> =
        ApiState(ApiState.Status.LOADING, null, null, null)

    //Create Post
    fun createPost(params: Map<String, RequestBody>, filePart: MultipartBody.Part?) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                post.value = _post.loading()
                timelineRepository.createPost(params = params, filePart)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        post.value = _post.success(it.value.success)
                    }
                    is Resource.Failure->{
                        ProgressDialog.hideProgressDialog()
                        handleApiError(failure = it)
                    }
                    else -> Unit
                }
            }.onFailure {
                post.value = _post.error(it)
            }
        }
    }//Create Post

    fun likeUnLikePost(postId: String, isLike: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                post.value = _post.loading()
                if (isLike)
                    timelineRepository.likePost(postId = postId)
                else
                    timelineRepository.unLikePost(postId = postId)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        post.value = _post.success(it.value.success)
                    }

                    is Resource.Failure->{
                        ProgressDialog.hideProgressDialog()
                        handleApiError(failure = it)
                    }
                    else -> Unit
                }
            }.onFailure {
                post.value = _post.error(it)
            }
        }
    }

    fun getPosts() = Pager(config = PagingConfig(100, enablePlaceholders = false)) {
        PaginationPosts(timelineRepository)
    }.flow.cachedIn(viewModelScope)

}