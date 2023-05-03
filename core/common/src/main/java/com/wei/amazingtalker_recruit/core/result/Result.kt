package com.wei.amazingtalker_recruit.core.result

import okhttp3.ResponseBody

sealed class Result<out T> {

    data class Success<out T>(val value: T) : Result<T>()

    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Result<Nothing>()

    data class Exception(
        val exceptionMessage: String?
    ) : Result<Nothing>()
}