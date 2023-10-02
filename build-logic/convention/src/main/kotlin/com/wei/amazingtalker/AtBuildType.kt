package com.wei.amazingtalker

/**
 * This is shared between :app module to provide configurations type safety.
 */
enum class AtBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE
}
