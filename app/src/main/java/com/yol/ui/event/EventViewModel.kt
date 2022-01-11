package com.yol.ui.event

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yol.di.Resource
import com.yol.network.repositories.EventRepository
import com.yol.network.repositories.pagination.PaginationEvents
import com.yol.network.request.CreateEventRequest
import com.yol.utils.ApiState
import com.yol.utils.ProgressDialog
import com.yol.utils.handleApiError
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    var createEvent = MutableLiveData<ApiState<Boolean>>()
    private val _createEvent: ApiState<Boolean> =
        ApiState(ApiState.Status.LOADING, null, null, null)

    fun createEvent(createEventRequest: CreateEventRequest) {
        viewModelScope.launch {
            runCatching {
                createEvent.value = _createEvent.loading()
                eventRepository.createEvent(createEventRequest = createEventRequest)
            }.onSuccess {
                when (it) {
                    is Resource.Success -> {
                        createEvent.value = _createEvent.success(it.value.success)
                    }
                    is Resource.Failure->{
                        ProgressDialog.hideProgressDialog()
                        handleApiError(failure = it)
                    }
                    else -> Unit
                }
            }.onFailure {
                createEvent.value = _createEvent.error(it)
            }
        }
    }

    fun getEvents() = Pager(config = PagingConfig(10, enablePlaceholders = false)) {
        PaginationEvents(eventRepository)
    }.flow.cachedIn(viewModelScope)

}