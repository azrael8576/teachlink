package com.wei.teachlink.core.data.repository

import com.wei.teachlink.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the user's currently token
     */
    suspend fun setTokenString(tokenString: String)
}
