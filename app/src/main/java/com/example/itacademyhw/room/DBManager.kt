package com.example.itacademyhw.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itacademyhw.room.DAO.UserDao
import com.example.itacademyhw.room.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class DBManager : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: DBManager? = null

        fun getDatabase(context: Context): DBManager {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        DBManager::class.java,
                        "room-user-db"
                    )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}