package com.wei.amazingtalker_recruit.core.network.di

import com.wei.amazingtalker_recruit.core.network.AtNetworkDataSource
import com.wei.amazingtalker_recruit.core.network.retrofit.RetrofitAtNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * TODO 移動至 build variants 下產出資料夾
 */
@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {

    @Binds
    fun binds(implementation: RetrofitAtNetwork): AtNetworkDataSource
}
