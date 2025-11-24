package com.dataagrin.project.repository

import com.dataagrin.project.model.WeatherEntity

data class WeatherResult(
    val data: WeatherEntity?,
    val isCached: Boolean,
    val error: String? = null
)
