package com.wei.teachlink.core.network.di

import com.wei.teachlink.core.network.TlNetworkDataSource
import com.wei.teachlink.core.network.retrofit.RetrofitTlNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {
    @Binds
    fun binds(implementation: RetrofitTlNetwork): TlNetworkDataSource
}
