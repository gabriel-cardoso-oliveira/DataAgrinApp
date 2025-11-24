package com.dataagrin.project.repository

import com.dataagrin.project.data.local.AppDao
import com.dataagrin.project.model.Task
import com.dataagrin.project.model.TaskStatus
import kotlinx.coroutines.flow.Flow

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