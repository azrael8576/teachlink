package com.wei.amazingtalker_recruit.api

import com.wei.amazingtalker_recruit.data.AmazingtalkerTeacherScheduleResponse
import com.wei.amazingtalker_recruit.utilities.AMAZING_TALKER_SERVICE_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Used to connect to the Amazingtalker API to fetch teacher's schedule
 */
interface AmazingtalkerService {

    /**
     * https://en.amazingtalker.com/v1/guest/teachers/jamie-coleman/schedule?started_at=2023-02-26T16:00:00.000Z
     */
    @GET("teachers/{teacherName}/schedule")
    suspend fun searchTeacherSchedule(
        @Path("teacherName") teacherName: String,
        @Query("started_at") started_at: String,
    ): AmazingtalkerTeacherScheduleResponse

    companion object {
        private const val BASE_URL = AMAZING_TALKER_SERVICE_BASE_URL

        fun create(): AmazingtalkerService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AmazingtalkerService::class.java)
        }
    }
}
