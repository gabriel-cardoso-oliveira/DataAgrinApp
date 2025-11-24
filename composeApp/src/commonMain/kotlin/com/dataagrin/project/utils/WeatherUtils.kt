package com.dataagrin.project.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(weatherCode: Int): ImageVector {
    return when (weatherCode) {
        0 -> Icons.Default.WbSunny
        1, 2, 3 -> Icons.Default.Cloud
        51, 53, 55 -> Icons.Default.Grain
        56, 57 -> Icons.Default.AcUnit
        61, 63, 65 -> Icons.Default.Umbrella
        66, 67 -> Icons.Default.Snowshoeing
        77 -> Icons.Default.Grain
        80, 81, 82 -> Icons.Default.Thunderstorm
        95 -> Icons.Default.Thunderstorm
        96, 99 -> Icons.Default.Thunderstorm
        else -> Icons.Default.Thermostat
    }
}
