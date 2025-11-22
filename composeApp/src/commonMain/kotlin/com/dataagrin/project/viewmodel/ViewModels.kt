package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.model.*
import com.dataagrin.project.repository.*
import kotlinx.coroutines.flow.*
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

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {
    val activities = repository.activities.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addActivity(activity: ActivityLog) {
        viewModelScope.launch {
            repository.addActivity(activity)
        }
    }
}

data class WeatherUiState(
    val weather: WeatherEntity? = null,
    val isCached: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    fun loadWeather(lat: Double = -16.6, lon: Double = -49.2) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getWeather(lat, lon).collect { result ->
                _uiState.update {
                    it.copy(
                        weather = result.data,
                        isCached = result.isCached,
                        error = result.error,
                        isLoading = false
                    )
                }
            }
        }
    }
}
