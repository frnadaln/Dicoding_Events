package com.dicoding.dicodingevents

import android.content.Context
import com.dicoding.dicodingevents.database.EventDatabase
import com.dicoding.dicodingevents.repository.EventRepository
import com.dicoding.dicodingevents.retrofit.ApiConfig
import com.dicoding.dicodingevents.helper.AppExecutors

object Injection {
    fun provideRepository(context: Context) : EventRepository {
        val apiService = ApiConfig.getApiService()
        val db = EventDatabase.getDatabase(context)
        val dao = db.eventDao()
        val executor = AppExecutors()
        return EventRepository.getInstance(apiService, dao, executor)
    }
}
