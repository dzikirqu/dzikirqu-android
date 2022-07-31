import dependencies.Deci

plugins {
    id(BuildPlugins.COMMON_ANDROID_LIBRARY)
}

dependencies {
    implementation(Deci.Coroutine.CORE)
    implementation(Deci.Coroutine.ANDROID)
}