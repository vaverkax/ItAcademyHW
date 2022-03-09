package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BeerDataDB
import com.example.data.model.RemoteKeys

@Database(entities = [BeerDataDB::class, RemoteKeys::class], version = 1)
abstract class BeerDatabase: RoomDatabase() {

    abstract fun beerDao(): BeerDAO
    abstract fun remoteKeysDao(): RemoteKeysDAO

    companion object {
        fun create(context: Context) =
            Room.databaseBuilder(context, BeerDatabase::class.java, "beerdb.db")
                .build()
    }
}