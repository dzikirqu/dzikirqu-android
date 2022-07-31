
object Versions{
    val hilt = "2.31.2-alpha"
    val hiltViewModel = "1.0.0-alpha02"
    val hiltAndroidX = "1.0.0-beta01"
    val kotlin = "1.6.21"
    val coroutines = "1.3.9"
    val coreKtx = "1.3.2"
    val appCompatJetpack = "1.2.0"
    val constraintLayout = "2.0.4"
    val legacySupport = "1.0.0"
    val animatedVectorDrawable = "1.1.0"
    val cardView = "1.0.0"
    val multiDex = "2.0.1"
    val material = "1.3.0"
    val flexbox = "2.0.1"
    val room = "2.4.0-alpha01"
    val maps = "17.0.0"
    val lifecycleExtensions = "2.2.0"
    val lifecycleJava8Compiler = "1.1.1"
    val lifecycleViewModel = "2.3.0"
    val lifecycleCoroutines = "2.3.0"
    val cameraX = "1.0.0-alpha03"
    val glide = "4.11.0"
    val retrofit = "2.9.0"
    val gson = "2.8.4"
}

object hiltDependencies{
    val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltViewModel}"
    val hiltAndroidXCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltAndroidX}"
}

object jetpackDependencies{
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatJetpack}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    val animatedVectorDrawable = "androidx.vectordrawable:vectordrawable-animated:${Versions.animatedVectorDrawable}"
    val cardView ="androidx.cardview:cardview:${Versions.cardView}"
    val multiDex = "androidx.multidex:multidex:${Versions.multiDex}"
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    val lifecycleCompiler = "android.arch.lifecycle:common-java8:${Versions.lifecycleJava8Compiler}"
    val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModel}"
    val lifecycleCoroutines = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleCoroutines}"
    val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleExtensions}"
    val cameraXCore = "androidx.camera:camera-core:${Versions.cameraX}"
    val cameraX2 = "androidx.camera:camera-camera2:${Versions.cameraX}"
}

object retrofitDependencies{
    val gson = "com.google.code.gson:gson:${Versions.gson}"
    val retrofit= "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val gsonConverter =  "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
}

object glideDependencies{
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
}

object googleDependencies{
    val material ="com.google.android.material:material:${Versions.material}"
    val flexbox = "com.google.android:flexbox:${Versions.flexbox}"
    val maps = "com.google.android.gms:play-services-maps:${Versions.maps}"
}

object roomDependencies{
    val room =  "androidx.room:room-runtime:${Versions.room}"
    val compiler = "androidx.room:room-compiler:${Versions.room}"
    val ktx = "androidx.room:room-ktx:${Versions.room}"
}


object pagingDependencies{
    val paging = "androidx.paging:paging-runtime:3.0.0"
}

object chuckDependencies{
    val chuckDebug = "com.readystatesoftware.chuck:library:1.1.0"
    val chuckRelease = "com.readystatesoftware.chuck:library-no-op:1.1.0"
}

object kotlinDependencies{
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}