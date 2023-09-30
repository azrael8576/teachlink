package com.wei.amazingtalker_recruit

/**
 * This is shared between :app module to provide configurations type safety.
 */
enum class AtBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE
}
