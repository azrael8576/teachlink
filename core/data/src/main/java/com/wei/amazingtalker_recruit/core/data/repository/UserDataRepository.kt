package com.wei.amazingtalker_recruit.core.data.repository

import kotlinx.coroutines.flow.Flow
import com.wei.amazingtalker_recruit.core.model.data.UserData

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
