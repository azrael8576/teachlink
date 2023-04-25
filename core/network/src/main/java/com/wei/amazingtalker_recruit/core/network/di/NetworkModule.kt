package com.wei.amazingtalker_recruit.core.network.di

import com.wei.amazingtalker_recruit.core.network.AtNetworkDataSource
import com.wei.amazingtalker_recruit.core.network.retrofit.RetrofitAtNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindsAtNetworkDataSource(
      atNetworkDataSource: RetrofitAtNetwork
    ): AtNetworkDataSource
}