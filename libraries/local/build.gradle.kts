import dependencies.Deci

plugins {
    id(BuildPlugins.COMMON_ANDROID_LIBRARY)
}

dependencies {
    // Storage
    implementation(Deci.Local.HAWK)
}