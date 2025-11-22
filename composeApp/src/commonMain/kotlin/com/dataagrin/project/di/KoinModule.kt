package com.dataagrin.project.di

import com.dataagrin.project.data.local.AppDatabase
import com.dataagrin.project.data.local.getDatabaseBuilder
import com.dataagrin.project.data.local.getRoomDatabase
import com.dataagrin.project.data.remote.WeatherApi
import com.dataagrin.project.repository.ActivityRepository
import com.dataagrin.project.repository.TaskRepository
import com.dataagrin.project.repository.WeatherRepository
import com.dataagrin.project.viewmodel.ActivityViewModel
import com.dataagrin.project.viewmodel.TaskViewModel
import com.dataagrin.project.viewmodel.WeatherViewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single<AppDatabase> { getRoomDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().appDao() }

    // API
    single { WeatherApi() }

    // Repositories
    single { TaskRepository(get()) }
    single { ActivityRepository(get()) }
    single { WeatherRepository(get(), get()) }

    // ViewModels
    factory { TaskViewModel(get()) }
    factory { ActivityViewModel(get()) }
    factory { WeatherViewModel(get()) }
}