package com.dataagrin.project.data.local

import androidx.room.*
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dataagrin.project.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Tasks
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Query("UPDATE tasks SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: String, status: TaskStatus)

    // Activities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityLog)

    @Query("SELECT * FROM activities ORDER BY timestamp DESC")
    fun getActivities(): Flow<List<ActivityLog>>

    // Weather
    @Query("SELECT * FROM weather_cache LIMIT 1")
    suspend fun getWeatherCache(): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(weather: WeatherEntity)
}

@Database(entities = [Task::class, ActivityLog::class, WeatherEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .build()
}

expect fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<AppDatabase>
