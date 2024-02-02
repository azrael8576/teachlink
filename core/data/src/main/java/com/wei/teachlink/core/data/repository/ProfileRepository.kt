package com.wei.teachlink.core.data.repository

import com.wei.teachlink.core.model.data.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(userId: String): Flow<UserProfile>
}
