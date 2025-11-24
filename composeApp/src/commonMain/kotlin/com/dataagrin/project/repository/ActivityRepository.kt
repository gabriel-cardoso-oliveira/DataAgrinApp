package com.dataagrin.project.repository

import com.dataagrin.project.data.local.AppDao
import com.dataagrin.project.model.ActivityLog
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val dao: AppDao) {
    val activities: Flow<List<ActivityLog>> = dao.getActivities()
    suspend fun addActivity(activity: ActivityLog) = dao.insertActivity(activity)
}