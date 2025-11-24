package com.dataagrin.project.viewmodel

import com.dataagrin.project.model.WeatherEntity

data class WeatherUiState(
    val weather: WeatherEntity? = null,
    val isCached: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
