plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    flavorDimensions("default")

    defaultConfig {
        applicationId = "com.wagyufari.dzikirqu"
        minSdk = 23
        targetSdk = 30
        versionCode = 93
        versionName = "2.6.11"

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }

        missingDimensionStrategy("dimension", "prod", "dev")
    }

    setDynamicFeatures(mutableSetOf(":pagedquran"))

    sourceSets{
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("main") {
            res.setSrcDirs(
                arrayOf(
                    "src/main/res",
                    "src/main/res/layouts/goal",
                    "src/main/res/layouts/user", "src/main/kotlin"
                ).asIterable()
            )
        }
        create("dev"){
            res.srcDirs("src/dev/res", "src/dzikirquDev/res")
        }
    }

    kapt {
        correctErrorTypes = true
        generateStubs = true
    }

    lintOptions {
        warning("InvalidPackage")
        isCheckReleaseBuilds = false
    }

    productFlavors{
        create("prod"){
            dimension = "default"
            applicationId = "com.wagyufari.dzikirqu"


            buildConfigField(
                "String",
                "SERVER_URL",
                "\"https://api.dzikirqu.com/\""
            )
        }
        create("dev"){
            dimension = "default"
            applicationId = "com.wagyufari.dzikirqu.dev"

            buildConfigField(
                "String",
                "SERVER_URL",
                "\"https://api-development.dzikirqu.com/\""
            )
            buildConfigField(
                "String",
                "CDN_ALQURANCLOUD_SERVER_URL",
                "\"https://cdn.islamic.network/\""
            )
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions("default")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }

}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")

    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")


    implementation("com.google.dagger:dagger:2.38.1")
    implementation("com.google.firebase:firebase-analytics:17.2.2")
    kapt("com.google.dagger:dagger-compiler:2.38.1")
    //(Dagger Android
    api("com.google.dagger:dagger-android:2.37")
    api("com.google.dagger:dagger-android-support:2.37")
    kapt("com.google.dagger:dagger-android-processor:2.37")
    //(Dagger - Hilt)
    implementation("com.google.dagger:hilt-android:2.38.1")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")

    implementation(kotlinDependencies.kotlin)
    implementation(kotlinDependencies.coroutinesCore)
    implementation(kotlinDependencies.coroutinesAndroid)

    implementation(jetpackDependencies.coreKtx)
    implementation(jetpackDependencies.appCompat)
    implementation(jetpackDependencies.constraintLayout)
    implementation(jetpackDependencies.legacySupport)
    implementation(jetpackDependencies.animatedVectorDrawable)
    implementation(jetpackDependencies.cardView)
    implementation(jetpackDependencies.multiDex)
    implementation(jetpackDependencies.lifecycleExtensions)
    implementation(jetpackDependencies.lifecycleCompiler)
    implementation(jetpackDependencies.lifecycleViewModel)
    implementation(jetpackDependencies.lifecycleLivedata)
    implementation(jetpackDependencies.lifecycleCoroutines)
    implementation(jetpackDependencies.cameraXCore)
    implementation(jetpackDependencies.cameraX2)

    implementation(retrofitDependencies.gson)
    implementation(retrofitDependencies.gsonConverter)
    implementation(retrofitDependencies.retrofit)

//    implementation(googleDependencies.material)
    implementation("com.google.android.material:material:1.3.0")
    implementation(googleDependencies.flexbox)
    implementation(googleDependencies.maps)

    kapt("org.xerial:sqlite-jdbc:3.34.0")

    implementation(roomDependencies.room)
    implementation(roomDependencies.ktx)
    kapt(roomDependencies.compiler)

    implementation(pagingDependencies.paging)


    implementation("com.github.Zhuinden:fragmentviewbindingdelegate-kt:1.0.0")

    // Activity KTX for viewModels()
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("android.arch.lifecycle:extensions:1.1.1")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.gms:play-services-maps:17.0.1")

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.1")

    implementation("com.github.msarhan:ummalqura-calendar:1.1.9")

    debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")

    implementation(platform("com.google.firebase:firebase-bom:30.1.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.2.0")

    implementation("androidx.work:work-runtime-ktx:2.7.0-alpha05")
    implementation("androidx.work:work-rxjava2:2.7.0-alpha05")

    implementation("com.google.android.play:core:1.10.2")

    implementation("com.pixplicity.easyprefs:EasyPrefs:1.10.0")

    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.github.SanojPunchihewa:InAppUpdater:1.0.5")

    implementation("net.danlew:android.joda:2.10.12.2")


    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.5.0-rc01")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.1.1")

    implementation("com.google.accompanist:accompanist-pager:0.23.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.23.0")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01")

    implementation("com.google.accompanist:accompanist-flowlayout:0.23.1")

    implementation("org.jetbrains:markdown:0.2.1")
    implementation("io.noties.markwon:html:4.6.2")
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:editor:4.6.2")

    implementation("net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:3.0.0-RC3")

    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")

    implementation("net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:3.0.0-RC3")

    val nav_version = "2.5.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation(glideDependencies.glide)
    kapt(glideDependencies.glideCompiler)

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

}

