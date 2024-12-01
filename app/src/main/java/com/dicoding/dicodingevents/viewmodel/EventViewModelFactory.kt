package com.dicoding.dicodingevents.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevents.repository.EventRepository
import com.dicoding.dicodingevents.Injection
import com.dicoding.dicodingevents.ui.PreferencesSetting
import com.dicoding.dicodingevents.ui.dataStore

@Suppress("UNCHECKED_CAST")
class EventViewModelFactory(
    private val preference : PreferencesSetting,
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EventViewModel::class.java) -> {
                EventViewModel(preference, eventRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class data : ${modelClass.name} please create new ViewModel at factory")
        }
    }

    companion object {
        @Volatile
        private var instance : EventViewModelFactory? = null

        fun getInstance(context: Context) : EventViewModelFactory =
            instance ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                val preference = PreferencesSetting.getInstance(context.dataStore)
                instance ?:EventViewModelFactory(preference, repository)
            }.also { instance =  it }
    }
}
