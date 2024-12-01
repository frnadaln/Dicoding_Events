package com.dicoding.dicodingevents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevents.database.EventEntity
import com.dicoding.dicodingevents.repository.EventRepository
import com.dicoding.dicodingevents.helper.Result
import com.dicoding.dicodingevents.ui.PreferencesSetting
import kotlinx.coroutines.launch

class EventViewModel(
    private val preferencesSetting: PreferencesSetting,
    private val eventRepository: EventRepository
) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return preferencesSetting.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            preferencesSetting.saveThemeSetting(isDarkMode)
        }
    }

    fun getNotificationSetting(): LiveData<Boolean> {
        return preferencesSetting.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isActive: Boolean) {
        viewModelScope.launch {
            preferencesSetting.saveNotificationSetting(isActive)
        }
    }

    fun searchEvent(query: String): LiveData<Result<List<EventEntity>>> {
        return eventRepository.searchEvent(query)
    }

    fun getActiveEvent(): LiveData<Result<List<EventEntity>>> {
        return eventRepository.getAllEvents(1)
    }

    fun getCompletedEvent(): LiveData<Result<List<EventEntity>>> {
        return eventRepository.getAllEvents(0)
    }

    fun getFavoriteEvent(): LiveData<List<EventEntity>> {
        return eventRepository.getFavoriteEvent()
    }

    fun addEventToFavorite(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(event, true)
        }
    }

    fun removeEventFromFavorite(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(event, false)
        }
    }
}
