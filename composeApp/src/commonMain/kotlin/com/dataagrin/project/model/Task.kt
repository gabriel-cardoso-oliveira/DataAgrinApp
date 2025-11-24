package com.dataagrin.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskStatus { PENDING, IN_PROGRESS, COMPLETED }

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String,
    val title: String,
    val area: String,
    val time: String,
    val status: TaskStatus
)
