package com.dataagrin.project.repository

import com.dataagrin.project.data.local.AppDao
import com.dataagrin.project.data.remote.WeatherApi
import com.dataagrin.project.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TaskRepository(private val dao: AppDao) {
    val tasks: Flow<List<Task>> = dao.getTasks()

    suspend fun addTask(task: Task) = dao.insertTask(task)

    suspend fun updateStatus(id: String, status: TaskStatus) = dao.updateTaskStatus(id, status)

    suspend fun seedInitialData() {
        val initial = listOf(
            Task("1", "Plantio de milho", "Setor 1", "08:00", TaskStatus.PENDING),
            Task("2", "Verificação de pragas", "Setor 4", "10:30", TaskStatus.IN_PROGRESS),
            Task("3", "Verificação de irrigação", "Setor 5", "14:00", TaskStatus.PENDING)
        )
        dao.insertTasks(initial)
    }
}

class ActivityRepository(private val dao: AppDao) {
    val activities: Flow<List<ActivityLog>> = dao.getActivities()
    suspend fun addActivity(activity: ActivityLog) = dao.insertActivity(activity)
}

data class WeatherResult(
    val data: WeatherEntity?,
    val isCached: Boolean,
    val error: String? = null
)

class WeatherRepository(private val api: WeatherApi, private val dao: AppDao) {

    @OptIn(ExperimentalTime::class)
    fun getWeather(lat: Double, lon: Double): Flow<WeatherResult> = flow {
        val cached = dao.getWeatherCache()
        if (cached != null) {
            emit(WeatherResult(cached, isCached = true))
        }

        try {
            val response = api.fetchWeather(lat, lon)

            val entity = WeatherEntity(
                temperature = response.current.temperature_2m,
                humidity = response.current.relative_humidity_2m,
                weatherCode = response.current.weather_code,
                lastUpdated = Clock.System.now().toEpochMilliseconds(),
                hourlyTime = response.hourly.time.take(24).joinToString(","),
                hourlyTemp = response.hourly.temperature_2m.take(24).joinToString(",")
            )

            dao.saveWeather(entity)
            emit(WeatherResult(entity, isCached = false))

        } catch (e: Exception) {
            if (cached == null) {
                emit(WeatherResult(null, isCached = false, error = e.message))
            }
        }
    }
}