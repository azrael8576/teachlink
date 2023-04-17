package com.wei.amazingtalker_recruit.di

import com.wei.amazingtalker_recruit.network.RetrofitAmazingtalkerNetwork
import com.wei.amazingtalker_recruit.network.RetrofitAmazingtalkerNetworkApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun bindsRetrofitAmazingtalkerNetworkApi(
        retrofitAmazingtalkerNetworkApi: RetrofitAmazingtalkerNetwork
    ): RetrofitAmazingtalkerNetworkApi

}