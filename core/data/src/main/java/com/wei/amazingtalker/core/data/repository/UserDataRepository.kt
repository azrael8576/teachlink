package com.wei.amazingtalker.core.data.repository

import com.wei.amazingtalker.core.model.data.UserData
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
