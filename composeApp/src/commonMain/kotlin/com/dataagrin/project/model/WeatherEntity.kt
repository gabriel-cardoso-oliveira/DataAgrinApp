package com.dataagrin.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "weather_cache")
@Serializable
data class WeatherEntity(
    @PrimaryKey val id: Int = 0,
    val temperature: Double,
    val humidity: Double,
    val weatherCode: Int,
    val lastUpdated: Long,
    val hourlyTime: String,
    val hourlyTemp: String
)
