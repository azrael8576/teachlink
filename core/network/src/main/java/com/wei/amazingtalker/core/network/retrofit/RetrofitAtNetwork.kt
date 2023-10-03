package com.wei.amazingtalker.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.wei.amazingtalker.core.network.AtNetworkDataSource
import com.wei.amazingtalker.core.network.BuildConfig
import com.wei.amazingtalker.core.network.model.NetworkTeacherSchedule
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Amazing Talker Network API
 */
interface RetrofitAtNetworkApi {
    /**
     * https://en.amazingtalker.com/v1/guest/teachers/jamie-coleman/schedule?started_at=2023-02-26T16:00:00.000Z
     */
    @GET("teachers/{teacherName}/schedule")
    suspend fun getTeacherAvailability(
        @Path("teacherName") teacherName: String,
        @Query("started_at") startedAt: String?,
    ): NetworkTeacherSchedule
}

private const val AtBaseUrl = BuildConfig.BACKEND_URL

/**
 * [Retrofit] backed [AtNetworkDataSource]
 */
@Singleton
class RetrofitAtNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : AtNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(AtBaseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitAtNetworkApi::class.java)

    override suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String?,
    ): NetworkTeacherSchedule {
        return networkApi.getTeacherAvailability(teacherName, startedAt)
    }
}
