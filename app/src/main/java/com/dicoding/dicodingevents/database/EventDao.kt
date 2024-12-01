package com.dicoding.dicodingevents.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Query("SELECt * FROM dicoding_events WHERE isCompleted = 1")
    fun getCompletedEvent() : LiveData<List<EventEntity>>

    @Query("SELECT * FROM dicoding_events WHERE isActive = 1")
    fun getActiveEvent() : LiveData<List<EventEntity>>

    @Query("SELECT * FROM dicoding_events WHERE isFavorite = 1")
    fun getFavoriteEvent() : LiveData<List<EventEntity>>

    @Query("DELETE FROM dicoding_events WHERE isFavorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM dicoding_events WHERE isFavorite = 1 AND name = :name)")
    suspend fun isEventFavorite(name : String) : Boolean

    @Query("""
        SELECT * FROM dicoding_events
        WHERE isCompleted = 1 
        AND(
            name LIKE '%' || :query || '%' OR
            description LIKE '%' || :query || '%' OR
            summary LIKE '%' || :query || '%' 
            )
    """)
    fun searchCompletedEvent(query : String) : LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventsData(events : List<EventEntity>)

    @Update
    suspend fun updateEventsData(events : EventEntity)
}
