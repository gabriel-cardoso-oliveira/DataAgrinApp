package com.dataagrin.project.di

import com.dataagrin.project.data.local.AppDatabase
import com.dataagrin.project.data.local.getDatabaseBuilder
import com.dataagrin.project.data.local.getRoomDatabase
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<AppDatabase> {
        getRoomDatabase(getDatabaseBuilder(null))
    }
}
