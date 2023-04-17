package com.wei.amazingtalker_recruit.network;

import com.wei.amazingtalker_recruit.data.AmazingtalkerTeacherScheduleResponse
import com.wei.amazingtalker_recruit.utilities.AMAZING_TALKER_SERVICE_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Amazingtalker Network API
 */
interface RetrofitAmazingtalkerNetworkApi {
    /**
     * https://en.amazingtalker.com/v1/guest/teachers/jamie-coleman/schedule?started_at=2023-02-26T16:00:00.000Z
     */
    @GET("teachers/{teacherName}/schedule")
    suspend fun searchTeacherSchedule(
        @Path("teacherName") teacherName: String,
        @Query("started_at") started_at: String,
    ): AmazingtalkerTeacherScheduleResponse
}

private const val BASE_URL = AMAZING_TALKER_SERVICE_BASE_URL

/**
 * [Retrofit] backed [AmazingtalkerNetworkDataSource]
 */
@Singleton
class RetrofitAmazingtalkerNetwork @Inject constructor() : RetrofitAmazingtalkerNetworkApi{

    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitAmazingtalkerNetworkApi::class.java)

    override suspend fun searchTeacherSchedule(
        teacherName: String,
        started_at: String
    ): AmazingtalkerTeacherScheduleResponse {
        return networkApi.searchTeacherSchedule(teacherName, started_at)
    }

}
