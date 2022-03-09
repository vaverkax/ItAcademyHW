package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.RemoteKeys

@Dao
interface RemoteKeysDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeys>)

    @Query("SELECT * FROM remoteKeys WHERE beerId = :beerId")
    suspend fun findBeerById(beerId: Long): RemoteKeys?

    @Query("DELETE FROM remoteKeys")
    suspend fun deleteAll()


}