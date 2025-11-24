package com.dataagrin.project.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<AppDatabase> {
    val androidContext = context as? Context ?: throw IllegalStateException("Context not available")
    val dbFile = androidContext.getDatabasePath("data_agrin.db")
    return Room.databaseBuilder<AppDatabase>(
        context = androidContext,
        name = dbFile.absolutePath
    )
}
