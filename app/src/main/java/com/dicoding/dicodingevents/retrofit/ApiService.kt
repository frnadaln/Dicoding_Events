package com.dicoding.dicodingevents.retrofit

import com.dicoding.dicodingevents.data.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiservice {
    @GET("events")
    suspend fun getEvents(
        @Query("active")
        active : Int
    ) : EventResponse

    @GET("events")
    suspend fun getUpdatedEvent(
        @Query("active")
        active: Int = -1,
        @Query("limit")
        limit : Int = 40
    ) : EventResponse
}
