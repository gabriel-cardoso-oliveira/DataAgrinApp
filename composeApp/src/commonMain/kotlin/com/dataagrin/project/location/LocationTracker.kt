package com.dataagrin.project.location

import dev.icerock.moko.geo.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}
