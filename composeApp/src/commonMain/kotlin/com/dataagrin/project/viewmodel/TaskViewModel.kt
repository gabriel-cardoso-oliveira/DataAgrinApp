package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.model.TaskStatus
import com.dataagrin.project.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val tasks = repository.tasks.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            if (tasks.value.isEmpty()) repository.seedInitialData()
        }
    }

    fun updateStatus(id: String, status: TaskStatus) {
        viewModelScope.launch {
            repository.updateStatus(id, status)
        }
    }
}