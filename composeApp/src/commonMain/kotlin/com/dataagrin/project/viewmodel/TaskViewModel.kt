package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.model.Task
import com.dataagrin.project.model.TaskStatus
import com.dataagrin.project.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface TaskUiState {
    data class Success(val tasks: List<Task>) : TaskUiState
    data object Loading : TaskUiState
    data object Error : TaskUiState
}

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    val uiState: StateFlow<TaskUiState> = repository.tasks
        .map { tasks -> if (tasks.isEmpty()) TaskUiState.Loading else TaskUiState.Success(tasks) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskUiState.Loading
        )

    init {
        viewModelScope.launch {
            repository.seedInitialData()
        }
    }

    fun updateStatus(id: String, status: TaskStatus) {
        viewModelScope.launch {
            repository.updateStatus(id, status)
        }
    }
}