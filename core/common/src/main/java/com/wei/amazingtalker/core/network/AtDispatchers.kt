package com.wei.amazingtalker.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val atDispatcher: AtDispatchers)

enum class AtDispatchers {
    Default,
    IO,
}
