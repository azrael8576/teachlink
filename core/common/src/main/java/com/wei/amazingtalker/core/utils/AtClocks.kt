package com.wei.amazingtalker.core.utils

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Clocks(val atClock: AtClocks)

enum class AtClocks {
    DefaultClock,
    UtcClock,
}
