package com.dataagrin.project.repository

import com.dataagrin.project.data.local.AppDao
import com.dataagrin.project.model.ActivityLog
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ActivityRepository(private val dao: AppDao, private val postgrest: Postgrest) {

    private val activitiesTable = postgrest.from("activities")

    val activities: Flow<List<ActivityLog>> = dao.getActivities().onStart { syncActivities() }

    private suspend fun syncActivities() {
        try {
            val remoteActivities = activitiesTable.select().decodeList<ActivityLog>()
            dao.insertActivities(remoteActivities)
        } catch (e: Exception) {
            println("Sync activities failed: ${e.message}")
        }
    }

    suspend fun addActivity(activity: ActivityLog) {
        dao.insertActivity(activity)
        try {
            activitiesTable.insert(activity)
        } catch (e: Exception) {
            println("Add activity to Supabase failed: ${e.message}")
        }
    }
}