package com.wei.amazingtalker.core.data.repository

import com.wei.amazingtalker.core.model.data.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getUserProfile(userId: String): Flow<UserProfile>
}
