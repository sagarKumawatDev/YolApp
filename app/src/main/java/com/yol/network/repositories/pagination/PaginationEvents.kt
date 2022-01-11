package com.yol.network.repositories.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yol.network.repositories.EventRepository
import com.yol.network.response.EventResponseModel
import com.yol.utils.ProgressDialog
import retrofit2.HttpException
import java.io.IOException

class PaginationEvents constructor(
    private val eventRepository: EventRepository
) : PagingSource<Int, EventResponseModel.Record>() {
    private val DEFAULT_PAGE_INDEX = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EventResponseModel.Record> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = eventRepository.getEvents(page = page)
            ProgressDialog.hideProgressDialog()
            LoadResult.Page(
                response.data.records,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.data.records.isEmpty() || response.data.records.size < 10) null else page + 1
            )
        } catch (exception: IOException) {
            ProgressDialog.hideProgressDialog()
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            ProgressDialog.hideProgressDialog()
            LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, EventResponseModel.Record>): Int? {
        return null
    }
}