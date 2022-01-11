package com.yol.network.repositories.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yol.network.repositories.EventRepository
import com.yol.network.repositories.TimelineRepository
import com.yol.network.response.EventResponseModel
import com.yol.network.response.PostResponseModel
import com.yol.utils.ProgressDialog
import retrofit2.HttpException
import java.io.IOException

class PaginationPosts constructor(
    private val timelineRepository: TimelineRepository
) : PagingSource<Int, PostResponseModel.Record>() {
    private val DEFAULT_PAGE_INDEX = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostResponseModel.Record> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = timelineRepository.getPosts(page = page)
            ProgressDialog.hideProgressDialog()
            LoadResult.Page(
                response.data.records,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = null
            )
        } catch (exception: IOException) {
            ProgressDialog.hideProgressDialog()
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            ProgressDialog.hideProgressDialog()
            LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, PostResponseModel.Record>): Int? {
        return null
    }
}