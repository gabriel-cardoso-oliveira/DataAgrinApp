package com.dataagrin.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dataagrin.project.location.LocationTracker
import com.dataagrin.project.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                repository.getWeather(location.coordinates.latitude, location.coordinates.longitude).collect { result ->
                    _uiState.update {
                        it.copy(
                            weather = result.data,
                            isCached = result.isCached,
                            error = result.error,
                            isLoading = false
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Não foi possível obter a localização. Certifique-se de conceder as permissões necessárias e ativar o GPS."
                    )
                }
            }
        }
    }
}
