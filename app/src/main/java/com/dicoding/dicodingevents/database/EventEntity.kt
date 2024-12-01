package com.dicoding.dicodingevents.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "dicoding_events")
@Parcelize
data class EventEntity(
    @PrimaryKey
    val id : Int,

    @field:ColumnInfo(name = "name")
    val name : String,
    @field:ColumnInfo(name = "summary")
    val summary : String,
    @field:ColumnInfo(name = "description")
    val description : String,
    @field:ColumnInfo(name = "imageLogo")
    val imageLogo : String,
    @field:ColumnInfo(name = "mediaCover")
    val mediaCover : String,
    @field:ColumnInfo(name = "category")
    val category : String,
    @field:ColumnInfo(name = "ownerName")
    val ownerName : String,
    @field:ColumnInfo(name = "cityName")
    val cityName : String,
    @field:ColumnInfo(name = "quota")
    val quota : Int,
    @field:ColumnInfo(name = "registrant")
    val registrants : Int,
    @field:ColumnInfo(name = "beginTime")
    val beginTime : String,
    @field:ColumnInfo(name = "endTime")
    val endTime : String,
    @field:ColumnInfo(name =" link")
    val link : String,
    @field:ColumnInfo(name = "isFavorite")
    var isFavorite : Boolean,
    @field:ColumnInfo(name = "isCompleted")
    val isCompleted : Boolean,
    @field:ColumnInfo(name = "isActive")
    val isActive: Boolean
) : Parcelable
