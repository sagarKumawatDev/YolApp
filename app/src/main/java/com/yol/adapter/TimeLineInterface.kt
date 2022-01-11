package com.yol.adapter

import com.yol.model.CreatedBy
import com.yol.model.Post
import com.yol.network.response.PostResponseModel

interface TimeLineInterface {
    fun like( post: PostResponseModel.Record?, pos: Int)
    fun unLikePost( post: PostResponseModel.Record?, pos: Int)
    fun share(post: PostResponseModel.Record?, pos: Int)
}