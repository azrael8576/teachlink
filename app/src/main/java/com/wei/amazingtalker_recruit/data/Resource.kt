package com.wei.amazingtalker_recruit.data

import okhttp3.ResponseBody

sealed class Resource<out T> {

    data class Success<out T>(val value : T) : Resource<T>()

    data class Failure  (
        val isNetworkError: Boolean,
        val errorCode : Int?,
        val errorBody : ResponseBody?
    ) : Resource<Nothing>()

    data class Exception  (
        val exceptionMessage : String?
    ) : Resource<Nothing>()
}