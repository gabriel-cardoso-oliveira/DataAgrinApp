package com.dataagrin.project.location

import dev.icerock.moko.geo.Location

class IosLocationTracker : LocationTracker {
    override suspend fun getCurrentLocation(): Location? {
        // TODO: Implement actual location tracking for iOS
        return null
    }
}
