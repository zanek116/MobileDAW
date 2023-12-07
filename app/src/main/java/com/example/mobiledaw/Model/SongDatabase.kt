package com.example.mobiledaw.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Song::class], version = 1, exportSchema = false)
//@TypeConverters(plzWork::class)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getInstance(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "DAW_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}