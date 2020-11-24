package com.example.pickerimagevideoandfiles.data.source.local.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pickerimagevideoandfiles.app.MyApp
import com.example.pickerimagevideoandfiles.data.source.local.room.dao.FileDao
import com.example.pickerimagevideoandfiles.data.source.local.room.entities.FileData

@Database(entities = [FileData::class], version = 1)
//@TypeConverters(CustomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    MyApp.instance,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}