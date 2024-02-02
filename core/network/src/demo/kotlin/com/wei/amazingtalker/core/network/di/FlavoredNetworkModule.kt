package com.wei.amazingtalker.core.network.di

import com.wei.amazingtalker.core.network.AtNetworkDataSource
import com.wei.amazingtalker.core.network.fake.FakeRetrofitAtNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {
    @Binds
    fun binds(implementation: FakeRetrofitAtNetwork): AtNetworkDataSource
}
