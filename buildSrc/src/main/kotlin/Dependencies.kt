/**
 * This file contains all the dependencies we need in our project. Because we placed
 * them inside the buildSrc module we get auto code completion inside the rest of the project.
 * More information about this dependency management technique is available at:
 * https://handstandsam.com/2018/02/11/kotlin-buildsrc-for-better-gradle-dependency-management/
 *
 * @author Dennis Wehrle
 */
@Suppress("unused")
object Versions {

    // Android
    const val androidCompileSdk = 28
    const val androidMinSdk = 16
    const val androidTargetSdk = 28

    const val constraintLayout = "1.1.3"
    const val supportLibrary = "28.0.0"
    const val supportAndroidX = "1.0.0"

    // App Libraries
    const val dagger = "2.21"
    const val glassfishAnnotation = "10.0-b28"
    const val glide = "4.8.0"
    const val gson = "2.8.1"
    const val javaxAnnotation = "1.0"
    const val javaxInject = "1"
    const val kotlin = "1.2.41"
    const val leakCanary = "1.5.4"
    const val okHttp = "3.8.1"
    const val retrofit = "2.5.0"
    const val room = "1.0.0"
    const val rxAndroid = "2.1.0"
    const val rxJava = "2.2.0"
    const val rxKotlin = "2.2.0"
    const val stetho = "1.5.0"
    const val timber = "4.6.1"
    const val pageIndicatorView = "0.2.0"
    const val lifecycle = "1.1.1"
    const val lifecycleCompiler = "1.1.1"
    const val gander = "2.0.3"
    // Test Libraries
    const val androidSupportRules = "1.0.0"
    const val androidSupportRunner = "1.0.0"
    const val assertJ = "3.8.0"
    const val dexmakerMockito = "2.16.0"
    const val dexopener = "0.12.1"
    const val espresso = "3.0.1"
    const val jUnit = "4.12"
    const val mockitoKotlin = "1.5.0"
    const val robolectric = "3.4.2"
    const val runner = "0.5"
    const val facebook = "[5,6)"
    const val googlePlayServices = "17.0.0"
    const val googleFireBase = "16.0.6"
}

@Suppress("unused")
object AppDependencies {

    const val appcompatAndroidX = "androidx.appcompat:appcompat:${Versions.supportAndroidX}"
    const val recycleViewAndroidX = "androidx.recyclerview:recyclerview:${Versions.supportAndroidX}"
    const val constrainLayoutAndroidX = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val MultipleDexAndroidX = "androidx.multidex:multidex:2.0.1"
    const val KTXtAndroidX = "androidx.core:core-ktx:${Versions.supportAndroidX}"
    const val androidSupportAnnotations = "com.android.support:support-annotations:${Versions.supportLibrary}"

    const val facebookSDK = "com.facebook.android:facebook-android-sdk:${Versions.facebook}"
    const val facebookAccountKitSDK = "com.facebook.android:account-kit-sdk:${Versions.facebook}"

    const val googlePlayServiceLocation = "com.google.android.gms:play-services-location:${Versions.googlePlayServices}"
    const val googlePlayServiceMap = "com.google.android.gms:play-services-maps:${Versions.googlePlayServices}"
    const val googleFireBaseCore = "com.google.firebase:firebase-core:${Versions.googleFireBase}"
    const val googleFireBaseMessaging = "com.google.firebase:firebase-messaging:19.0.1"

    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"

    const val glassfishAnnotation = "org.glassfish:javax.annotation:${Versions.glassfishAnnotation}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideAnnotation = "com.github.bumptech.glide:annotations:${Versions.glide}"
    const val glideExtension = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val javaxAnnotation = "javax.annotation:jsr250-api:${Versions.javaxAnnotation}"
    const val javaxInject = "javax.inject:javax.inject:${Versions.javaxInject}"

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    const val roomCompiler = "android.arch.persistence.room:compiler:${Versions.room}"
    const val roomRuntime = "android.arch.persistence.room:runtime:${Versions.room}"
    const val roomRxJava = "android.arch.persistence.room:rxjava2:${Versions.room}"

    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"

    const val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
    const val lifecycleCompiler = "android.arch.lifecycle:compiler:${Versions.lifecycleCompiler}"

    const val sdp = "com.intuit.sdp:sdp-android:1.0.6"
    const val ssp = "com.intuit.ssp:ssp-android:1.0.6"

    const val leakCanaryDebug = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    const val leakCanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"

    const val stetho = "com.facebook.fresco:stetho:${Versions.stetho}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val gander = "com.ashokvarma.android:gander:${Versions.gander}"

    const val calligraphy = "uk.co.chrisjenx:calligraphy:2.2.0"
}

@Suppress("unused")
object TestDependencies {

    const val androidRules = "com.android.support.test:rules:${Versions.runner}"
    const val androidRunner = "com.android.support.test:runner:${Versions.runner}"
    const val assertj = "org.assertj:assertj-core:${Versions.assertJ}"
    const val dexmakerMockito = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.dexmakerMockito}"
    const val dexopener = "com.github.tmurakami:dexopener:${Versions.dexopener}"
    const val espressoContrib = "com.android.support.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espressoCore = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoIntents = "com.android.support.test.espresso:espresso-intents:${Versions.espresso}"
    const val junit = "junit:junit:${Versions.jUnit}"
    const val kotlinJUnit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val mockitoKotlin = "com.nhaarman:mockito-kotlin:${Versions.mockitoKotlin}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val roomTesting = "android.arch.persistence.room:testing:${Versions.room}"
    const val supportRules = "com.android.support.test:rules:${Versions.androidSupportRules}"
    const val supportRunner = "com.android.support.test:runner:${Versions.androidSupportRunner}"
} 
