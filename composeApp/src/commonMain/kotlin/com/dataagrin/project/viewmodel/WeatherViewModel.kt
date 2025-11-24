package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
