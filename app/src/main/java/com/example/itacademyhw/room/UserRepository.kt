package com.example.itacademyhw.room

import androidx.annotation.WorkerThread
import com.example.itacademyhw.room.DAO.UserDao
import com.example.itacademyhw.room.model.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<UserEntity>> = userDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: UserEntity) {
        userDao.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(user: UserEntity) {
        userDao.delete(user)
    }
}