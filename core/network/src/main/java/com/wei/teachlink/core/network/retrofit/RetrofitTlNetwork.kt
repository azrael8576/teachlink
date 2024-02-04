package com.wei.teachlink.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.wei.teachlink.core.network.BuildConfig
import com.wei.teachlink.core.network.TlNetworkDataSource
import com.wei.teachlink.core.network.model.NetworkTeacherSchedule
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
 * Retrofit API declaration for TeachLink Network API
 */
interface RetrofitTlNetworkApi {
    /**
     * ${BACKEND_URL}/teachers/jamie-coleman/schedule?started_at=2023-02-26T16:00:00.000Z
     */
    @GET("teachers/{teacherName}/schedule")
    suspend fun getTeacherAvailability(
        @Path("teacherName") teacherName: String,
        @Query("started_at") startedAt: String?,
    ): NetworkTeacherSchedule
}

private const val AT_BASE_URL = BuildConfig.BACKEND_URL

/**
 * [Retrofit] backed [TlNetworkDataSource]
 */
@Singleton
class RetrofitTlNetwork
@Inject
constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : TlNetworkDataSource {
    private val networkApi =
        Retrofit.Builder()
            .baseUrl(AT_BASE_URL)
            .callFactory(okhttpCallFactory)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitTlNetworkApi::class.java)

    override suspend fun getTeacherAvailability(
        teacherName: String,
        startedAt: String?,
    ): NetworkTeacherSchedule {
        return networkApi.getTeacherAvailability(teacherName, startedAt)
    }
}
