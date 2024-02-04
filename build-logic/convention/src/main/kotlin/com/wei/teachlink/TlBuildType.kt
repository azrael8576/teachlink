package com.wei.teachlink

/**
 * This is shared between :app module to provide configurations type safety.
 */
enum class TlBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
