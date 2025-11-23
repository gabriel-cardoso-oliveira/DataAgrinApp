package com.dataagrin.project.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dataagrin.project.ContextProvider

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val ctx = ContextProvider.context ?: throw IllegalStateException("Context not initialized")
    val dbFile = ctx.getDatabasePath("data_agrin.db")
    return Room.databaseBuilder<AppDatabase>(
        context = ctx,
        name = dbFile.absolutePath
    )
}