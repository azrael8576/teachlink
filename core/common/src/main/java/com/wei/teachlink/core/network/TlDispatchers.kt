package com.wei.teachlink.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val tlDispatcher: TlDispatchers)

enum class TlDispatchers {
    Default,
    IO,
}
