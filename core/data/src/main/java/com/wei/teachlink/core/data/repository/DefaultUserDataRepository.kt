package com.wei.teachlink.core.data.repository

import com.wei.teachlink.core.datastore.TlPreferencesDataSource
import com.wei.teachlink.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository
@Inject
constructor(
    private val atPreferencesDataSource: TlPreferencesDataSource,
) : UserDataRepository {
    override val userData: Flow<UserData> =
        atPreferencesDataSource.userData

    override suspend fun setTokenString(tokenString: String) {
        atPreferencesDataSource.setTokenString(tokenString)
    }
}
