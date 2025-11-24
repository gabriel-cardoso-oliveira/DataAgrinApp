package com.dataagrin.project.di

import com.dataagrin.project.data.local.AppDatabase
import com.dataagrin.project.data.remote.WeatherApi
import com.dataagrin.project.repository.ActivityRepository
import com.dataagrin.project.repository.TaskRepository
import com.dataagrin.project.repository.WeatherRepository
import com.dataagrin.project.viewmodel.ActivityViewModel
import com.dataagrin.project.viewmodel.TaskViewModel
import com.dataagrin.project.viewmodel.WeatherViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import org.koin.dsl.module

val appModule = module {
    single { get<AppDatabase>().appDao() }

    // API & Clients
    single { WeatherApi() }
    single<SupabaseClient> { com.dataagrin.project.data.remote.SupabaseClient.client }
    single<Postgrest> { get<SupabaseClient>().postgrest }

    // Repositories
    single { TaskRepository(get(), get()) }
    single { ActivityRepository(get()) }
    single { WeatherRepository(get(), get()) }

    // ViewModels
    factory { TaskViewModel(get()) }
    factory { ActivityViewModel(get()) }
    factory { WeatherViewModel(get(), get()) }
}.plus(locationModule()).plus(databaseModule())
