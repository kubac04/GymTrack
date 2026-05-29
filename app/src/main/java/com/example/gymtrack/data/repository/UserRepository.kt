package com.example.gymtrack.data.repository

import com.example.gymtrack.data.local.dao.UserDao
import com.example.gymtrack.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUserProfile(): Flow<UserEntity?> = userDao.getUserProfile()

    suspend fun saveUserProfile(user: UserEntity) {
        userDao.insertProfile(user)
    }
}
