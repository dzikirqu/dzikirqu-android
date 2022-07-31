import dependencies.Deci

plugins {
    id(BuildPlugins.COMMON_ANDROID_LIBRARY)
}

dependencies {
    // Local storage
    implementation(project(BuildModules.Libraries.HAPPY_RESOURCE))
    implementation(project(BuildModules.Libraries.LOCAL))

    // Network
    implementation(Deci.Retrofit.RETROFIT)
    implementation(Deci.Retrofit.GSON_CONVERTER)
    implementation(Deci.Retrofit.OKHTTP_LOGGING)

    // Chuck
    debugImplementation(Deci.Chuck.DEBUG)
    releaseImplementation(Deci.Chuck.RELEASE)
}