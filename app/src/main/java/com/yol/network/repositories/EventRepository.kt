package com.yol.network.repositories

import com.yol.network.RestApiInterface
import com.yol.network.request.CreateEventRequest

class EventRepository(
    private val restApiInterface: RestApiInterface
) : BaseRepository(restApiInterface) {

    suspend fun createEvent(createEventRequest: CreateEventRequest) =
        safeApiCall { restApiInterface.createEvent(createEventRequest = createEventRequest) }

    suspend fun getEvents(page: Int) = restApiInterface.getEvents(page = page)

}