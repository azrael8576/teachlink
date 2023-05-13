package com.wei.amazingtalker_recruit.core.decoder.di

import com.wei.amazingtalker_recruit.core.decoder.StringDecoder
import com.wei.amazingtalker_recruit.core.decoder.UriDecoder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StringDecoderModule {
    @Binds
    abstract fun bindStringDecoder(uriDecoder: UriDecoder): StringDecoder
}
