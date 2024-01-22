package com.wei.amazingtalker.core.data.repository

import com.wei.amazingtalker.core.datastore.AtPreferencesDataSource
import com.wei.amazingtalker.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository
@Inject
constructor(
    private val atPreferencesDataSource: AtPreferencesDataSource,
) : UserDataRepository {
    override val userData: Flow<UserData> =
        atPreferencesDataSource.userData

    override suspend fun setTokenString(tokenString: String) {
        atPreferencesDataSource.setTokenString(tokenString)
    }
}
