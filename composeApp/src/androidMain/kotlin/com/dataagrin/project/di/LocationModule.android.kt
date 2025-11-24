package com.dataagrin.project.di

import com.dataagrin.project.location.AndroidLocationTracker
import com.dataagrin.project.location.LocationTracker
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun locationModule(): Module = module {
    single<LocationTracker> { AndroidLocationTracker(get()) }
}
