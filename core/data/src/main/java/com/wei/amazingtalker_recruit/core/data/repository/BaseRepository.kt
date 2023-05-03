package com.wei.amazingtalker_recruit.core.data.repository

import android.util.Log
import com.wei.amazingtalker_recruit.core.network.AtDispatchers
import com.wei.amazingtalker_recruit.core.network.Dispatcher
import com.wei.amazingtalker_recruit.core.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

interface BaseRepository {

    val ioDispatcher: CoroutineDispatcher

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T>
}

open class BaseRepositoryImpl @Inject constructor(
    @Dispatcher(AtDispatchers.IO) override val ioDispatcher: CoroutineDispatcher
) : BaseRepository {

    override suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {

        return withContext(ioDispatcher) {
            try {
                Result.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IllegalArgumentException -> {
                        Log.e("BaseRepository: ", throwable.message.toString())
                        Result.Exception(throwable.message.toString())
                    }

                    is HttpException -> {
                        Result.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }

                    else -> {
                        Result.Failure(true, null, null)
                    }
                }
            }
        }

    }
}