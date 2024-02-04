plugins {
    alias(libs.plugins.tl.android.library)
    alias(libs.plugins.tl.android.hilt)
}

android {
    namespace = "com.wei.teachlink.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    testImplementation(projects.core.testing)
}
