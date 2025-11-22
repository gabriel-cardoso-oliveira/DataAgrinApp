package com.dataagrin.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class TaskStatus { PENDING, IN_PROGRESS, COMPLETED }

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String,
    val title: String,
    val area: String,
    val time: String,
    val status: TaskStatus
)

@Entity(tableName = "activities")
data class ActivityLog(
    @PrimaryKey val id: String,
    val type: String,
    val area: String,
    val startTime: String,
    val endTime: String,
    val observations: String,
    val timestamp: Long
)

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