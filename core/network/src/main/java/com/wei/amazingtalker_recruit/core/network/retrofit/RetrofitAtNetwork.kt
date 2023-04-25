package com.wei.amazingtalker_recruit.core.network.retrofit;

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.wei.amazingtalker_recruit.core.network.AtNetworkDataSource
import com.wei.amazingtalker_recruit.core.network.model.NetworkTeacherSchedule
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

const val AMAZING_TALKER_SERVICE_BASE_URL = "https://tw.amazingtalker.com/v1/guest/"

/**
 * Retrofit API declaration for AmazingTalker Network API
 */
interface RetrofitAtNetworkApi {
    /**
     * https://en.amazingtalker.com/v1/guest/teachers/jamie-coleman/schedule?started_at=2023-02-26T16:00:00.000Z
     */
    @GET("teachers/{teacherName}/schedule")
    suspend fun getTeacherSchedule(
        @Path("teacherName") teacherName: String,
        @Query("started_at") startedAt: String?,
    ): NetworkTeacherSchedule
}

private const val BASE_URL = AMAZING_TALKER_SERVICE_BASE_URL

/**
 * [Retrofit] backed [AtNetworkDataSource]
 */
@Singleton
class RetrofitAtNetwork @Inject constructor() : AtNetworkDataSource {

    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    private val networkJson = Json {
        ignoreUnknownKeys = true
    }

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitAtNetworkApi::class.java)

    override suspend fun getTeacherSchedule(
        teacherName: String,
        startedAt: String?
    ): NetworkTeacherSchedule {
        return networkApi.getTeacherSchedule(teacherName, startedAt)
    }

}
