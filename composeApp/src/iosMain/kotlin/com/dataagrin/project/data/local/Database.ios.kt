package com.dataagrin.project.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory
import kotlin.reflect.KClass

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/data_agrin.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { AppDatabase::class.instantiateImpl() }
    )
}

private fun KClass<AppDatabase>.instantiateImpl(): AppDatabase {
    TODO("Not yet implemented")
}
