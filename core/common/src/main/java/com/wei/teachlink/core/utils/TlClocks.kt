package com.wei.teachlink.core.utils

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Clocks(val tlClock: TlClocks)

enum class TlClocks {
    DefaultClock,
    UtcClock,
}
