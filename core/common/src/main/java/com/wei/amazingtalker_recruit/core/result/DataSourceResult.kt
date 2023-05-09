package com.wei.amazingtalker_recruit.core.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface DataSourceResult<out T> {
    data class Success<T>(val data: T) : DataSourceResult<T>
    data class Error(val exception: Throwable? = null) : DataSourceResult<Nothing>
    object Loading : DataSourceResult<Nothing>
}

fun <T> Flow<T>.asDataSourceResult(): Flow<DataSourceResult<T>> {
    return this
        .map<T, DataSourceResult<T>> {
            DataSourceResult.Success(it)
        }
        .onStart { emit(DataSourceResult.Loading) }
        .catch { emit(DataSourceResult.Error(it)) }
}
