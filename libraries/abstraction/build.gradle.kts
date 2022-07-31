import dependencies.Deci

plugins {
    id(BuildPlugins.COMMON_ANDROID_LIBRARY)
}

android {
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    // Android
    implementation(Deci.Android.APP_COMPAT)
    implementation(Deci.Android.MATERIAL)
}