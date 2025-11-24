package com.dataagrin.project.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<AppDatabase> {
    val dbFile = NSHomeDirectory() + "/data_agrin.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile
    )
}
