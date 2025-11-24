package com.dataagrin.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
