package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.model.ActivityLog
import com.dataagrin.project.model.Task
import com.dataagrin.project.model.TaskStatus
import com.dataagrin.project.repository.ActivityRepository
import com.dataagrin.project.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel(), KoinComponent {
    private val taskRepository: TaskRepository by inject()
    val activities = repository.activities.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addActivity(activity: ActivityLog) {
        viewModelScope.launch {
            repository.addActivity(activity)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun addTaskFromActivity(type: String, area: String, start: String, end: String, obs: String) {
        viewModelScope.launch {
            val newId = Random.nextInt(100, 1000).toString()
            val task = Task(newId, type, area, start, TaskStatus.PENDING)
            taskRepository.addTask(task)

            val activity = ActivityLog(
                newId,
                type,
                area,
                start,
                end,
                obs,
                Clock.System.now().toEpochMilliseconds())
            repository.addActivity(activity)
        }
    }
}