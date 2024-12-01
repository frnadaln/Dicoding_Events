package com.dicoding.dicodingevents.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.dicodingevents.data.EventResponse
import com.dicoding.dicodingevents.database.EventEntity
import com.dicoding.dicodingevents.database.EventDao
import com.dicoding.dicodingevents.retrofit.Apiservice
import com.dicoding.dicodingevents.helper.AppExecutors
import com.dicoding.dicodingevents.helper.Result
import kotlinx.coroutines.withContext
import kotlinx.coroutines.asCoroutineDispatcher

class EventRepository private constructor(
    private val apiService: Apiservice,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun getAllEvents(active: Int): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active)

            val data = response.listEvents
            val eventList = data.map { event ->
                val isCompleted = active == 0
                val isActive = active == 1
                val isFavorite = event.name.let {
                    eventDao.isEventFavorite(it)
                }
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.description,
                    event.imageLogo,
                    event.mediaCover,
                    event.category,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    isFavorite,
                    isCompleted,
                    isActive
                )
            }

            eventDao.insertEventsData(eventList)
            emit(Result.Success(eventList))

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteEvent(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvent()
    }

    fun searchEvent(query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val dataLocal = eventDao.searchCompletedEvent(query).map { eventList ->
                if (eventList.isNotEmpty()) {
                    Result.Success(eventList)
                } else {
                    Result.Error("No events found for the keyword: $query")
                }
            }
            emitSource(dataLocal)
        } catch (exception: Exception) {
            emit(Result.Error(exception.message.toString()))
        }
    }

    suspend fun setFavoriteEvent(event: EventEntity, favorite: Boolean) {
        event.isFavorite = favorite
        withContext(appExecutors.diskIO.asCoroutineDispatcher()) {
            eventDao.updateEventsData(event)
        }
    }

    suspend fun getNearestEvent() : EventResponse? {
        val getEvent =  try {
            apiService.getUpdatedEvent(active = -1, limit = 1)
        } catch (e: Exception) {
            null
        }
        return getEvent
    }

    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(
            apiService: Apiservice,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: EventRepository(apiService, eventDao, appExecutors)
        }.also { INSTANCE = it }
    }
}
