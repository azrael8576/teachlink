package com.wei.amazingtalker_recruit.core.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * DataSourceResult 是一個封裝數據源結果的密封接口，它可能是成功(Success)、錯誤(Error)或正在加載(Loading)的狀態。
 * @param T 數據源結果的型別參數。
 */
sealed interface DataSourceResult<out T> {
    data class Success<T>(val data: T) : DataSourceResult<T>
    data class Error(val exception: Throwable? = null) : DataSourceResult<Nothing>
    object Loading : DataSourceResult<Nothing>
}

/**
 * 此擴展函數將一個 Flow<T> 轉換為一個 Flow<DataSourceResult<T>>，將流中的每個元素封裝為 DataSourceResult.Success，
 * 在流開始時發出 DataSourceResult.Loading，並在流出現錯誤時發出 DataSourceResult.Error。
 *
 * @return 一個新的 Flow<DataSourceResult<T>>，其元素是 DataSourceResult。
 */
fun <T> Flow<T>.asDataSourceResult(): Flow<DataSourceResult<T>> {
    return this
        .map<T, DataSourceResult<T>> {
            DataSourceResult.Success(it)
        }
        .onStart { emit(DataSourceResult.Loading) }
        .catch { emit(DataSourceResult.Error(it)) }
}
