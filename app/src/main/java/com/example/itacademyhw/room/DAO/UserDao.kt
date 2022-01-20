package com.example.itacademyhw.room.DAO

import androidx.room.*
import com.example.itacademyhw.room.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity ORDER BY last_name ASC")
    fun getAll(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)
}
