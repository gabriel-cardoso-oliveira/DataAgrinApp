package com.dataagrin.project.repository

import com.dataagrin.project.data.local.AppDao
import com.dataagrin.project.model.Task
import com.dataagrin.project.model.TaskStatus
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart

class TaskRepository(private val dao: AppDao, private val postgrest: Postgrest) {

    private val tasksTable = postgrest.from("tasks")

    val tasks: Flow<List<Task>> = dao.getTasks().onStart { syncTasks() }

    private suspend fun syncTasks() {
        try {
            val remoteTasks = tasksTable.select().decodeList<Task>()
            dao.insertTasks(remoteTasks)
        } catch (e: Exception) {
            println("Sync failed: ${e.message}")
        }
    }

    suspend fun addTask(task: Task) {
        dao.insertTask(task)
        try {
            tasksTable.insert(task)
        } catch (e: Exception) {
            println("Add task to Supabase failed: ${e.message}")
        }
    }

    suspend fun updateStatus(id: String, status: TaskStatus) {
        dao.updateTaskStatus(id, status)
        try {
            tasksTable.update({ "status" to status }) { filter {
                eq("id", id)
            } }
        } catch (e: Exception) {
            println("Update status in Supabase failed: ${e.message}")
        }
    }

    suspend fun seedInitialData() {
        if (dao.getTasks().first().isEmpty()) {
            val initial = listOf(
                Task("1", "Plantio de milho", "Setor 1", "08:00", TaskStatus.PENDING),
                Task("2", "Verificação de pragas", "Setor 4", "10:30", TaskStatus.IN_PROGRESS),
                Task("3", "Verificação de irrigação", "Setor 5", "14:00", TaskStatus.PENDING)
            )
            dao.insertTasks(initial)
            try {
                tasksTable.insert(initial)
            } catch (e: Exception) {
                println("Seed initial data to Supabase failed: ${e.message}")
            }
        }
    }
}