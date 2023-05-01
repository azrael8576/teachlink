package com.wei.amazingtalker_recruit.core.data.repository

import android.util.Log
import com.wei.amazingtalker_recruit.core.network.AtDispatchers
import com.wei.amazingtalker_recruit.core.network.Dispatcher
import com.wei.amazingtalker_recruit.core.result.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

interface BaseRepository {

    val ioDispatcher: CoroutineDispatcher

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T>
}

open class BaseRepositoryImpl @Inject constructor(
    @Dispatcher(AtDispatchers.IO) override val ioDispatcher: CoroutineDispatcher
) : BaseRepository {

    override suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {

        return withContext(ioDispatcher){
            try{
                Resource.Success(apiCall.invoke())
            } catch(throwable : Throwable){
                when(throwable){
                    is IllegalArgumentException -> {
                        Log.e("BaseRepository: ", throwable.message.toString())
                        Resource.Exception(throwable.message.toString())
                    }
                    is HttpException ->{
                        Resource.Failure(false,throwable.code(),throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true,null,null)
                    }
                }
            }
        }

    }
}