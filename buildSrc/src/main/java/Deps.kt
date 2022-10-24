import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

object Version {
    private val verInfo = buildVerInfo()

    object ClassPathVersion {
        const val gradleVersion = "7.2.1"
        const val kotlinVersion = "1.7.0"
        const val hiltVersion = "2.42"
    }

    const val compileSdk = 32
    const val applicationId = "com.sunny.module.stu"
    const val minSdk = 28
    const val targetSdk = 32
    val versionCode = verInfo.first
    val versionName = verInfo.second

    const val coreKtx = "1.8.0"
    const val appCompat = "1.4.2"
    const val fragment = "1.4.0"
    const val activity = "1.4.0"
    const val lifecycle = "2.4.1"

    const val hilt = ClassPathVersion.hiltVersion
    const val coroutines = "1.6.0"
    const val dataStore = "1.0.0"
    const val glide = "4.11.0"
    const val timber = "4.5.1"
    const val zxing = "3.4.0"
    const val work = "2.0.1"
    const val room = "2.0.0"
    const val palette = "1.0.0"
    const val okHttp3 = "3.14.7"
    const val retrofit = "2.8.0"
    const val mmkv = "1.2.14"

    // ui
    const val material = "1.6.1"
    const val constraintLayout = "2.1.4"
    const val viewpager2 = "1.0.0"
    const val navigation = "2.0.0-rc02"
    const val recyclerView = "1.3.0-alpha02"
    const val flexbox = "3.0.0"

    // test
    const val testJunit = "4.12"
    const val androidTestJunit = "1.1.3"
    const val androidTestEspresso = "3.4.0"
    const val leakcanary = "2.9.1"
    const val stetho = "1.3.1"
}

object Deps {

    const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Version.appCompat}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Version.fragment}"
    const val activityKtx = "androidx.activity:activity-ktx:${Version.activity}"

    const val lifecycleCommon =
        "androidx.lifecycle:lifecycle-common:${Version.lifecycle}"
    const val lifecycleService =
        "androidx.lifecycle:lifecycle-service:${Version.lifecycle}"
    const val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
    const val lifecycleCommonJava8 =
        "androidx.lifecycle:lifecycle-common-java8:${Version.lifecycle}"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val lifecycleLiveDataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    const val lifecycleViewModelSavedstate =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.lifecycle}"

    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"

    const val okhttp =
        "com.squareup.okhttp3:okhttp:${Version.okHttp3}"
    const val okhttpLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Version.okHttp3}"

    const val retrofit =
        "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val retrofitConverterGson =
        "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
    const val retrofitAdapterRxjava2 =
        "com.squareup.retrofit2:adapter-rxjava2:${Version.retrofit}"

    const val rxjava = "io.reactivex.rxjava2:rxjava:2.1.3"
    const val rxjavaRxandroid = "io.reactivex.rxjava2:rxandroid:2.0.1"

    const val glide =
        "com.github.bumptech.glide:glide:${Version.glide}"
    const val glideCompiler =
        "com.github.bumptech.glide:compiler:${Version.glide}"
    const val glideOkhttpIntegration =
        "com.github.bumptech.glide:okhttp3-integration:${Version.glide}"

    const val hiltAndroid =
        "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltAndroidCompiler =
        "com.google.dagger:hilt-android-compiler:${Version.hilt}"
    const val hiltAndroidTesting =
        "com.google.dagger:hilt-android-testing:${Version.hilt}"

    const val roomRuntime =
        "androidx.room:room-runtime:${Version.room}"
    const val roomCompiler =
        "androidx.room:room-compiler:${Version.room}"

    const val dataStore = "androidx.datastore:datastore-preferences:${Version.dataStore}"
    const val workRuntime = "androidx.work:work-runtime:${Version.work}"
    const val timber = "com.jakewharton.timber:timber:${Version.timber}"
    const val zxing = "com.google.zxing:javase:${Version.zxing}"
    const val palette = "androidx.palette:palette:${Version.palette}"
    const val mmkv = "com.tencent:mmkv:${Version.mmkv}"

    const val testJunit = "junit:junit:${Version.testJunit}"
    const val androidTestJunit =
        "androidx.test.ext:junit:${Version.androidTestJunit}"
    const val androidTestEspresso =
        "androidx.test.espresso:espresso-core:${Version.androidTestEspresso}"

    const val stetho = "com.facebook.stetho:stetho:${Version.stetho}"
    const val stethoOkhttp = "com.facebook.stetho:stetho-okhttp3:${Version.stetho}"
    const val debugLeakCanary =
        "com.squareup.leakcanary:leakcanary-android:${Version.leakcanary}"

    //ui
    const val material = "com.google.android.material:material:${Version.material}"
    const val flexbox = "com.google.android.flexbox:flexbox:${Version.flexbox}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Version.recyclerView}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Version.viewpager2}"
    const val navigationFragment = "androidx.navigation:navigation-fragment:${Version.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Version.navigation}"
}

fun getBuildTime(): String {
    return SimpleDateFormat("MMdd_HHmm", Locale.getDefault()).format(Date())
}

fun buildOutputApkName(prefix: String, name: String): String {
    val timeStr = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
    return "${prefix}_${name}_${Version.versionName}_${timeStr}.apk"
}

// 大版本以及大版本对应的commitCount
private const val baseVerName = "1.1"
private const val baseVerCode = 300

// 上次发布大版本后提交的次数
private fun getCommitCount(): Int {
    val count = "git rev-list --count HEAD".exec().toInt()
    val i = if (count > baseVerCode) {
        count - baseVerCode
    } else {
        count
    }
    println("VerInfo-getCommitCount :$i")
    return i
}

/*
name = 1.0.(commitCount / 100).(commitCount % 100)
code = 100 * 10000 + commitCount
 */
private fun buildVerInfo(): Pair<Int, String> {
    val commitCount = getCommitCount()
    // 1.0 >>> 100
    val baseCode = baseVerName.replace(".", "0").toInt()
    // 1000345 = 100*10000 + 345
    val code = baseCode * 10000 + commitCount

    // 345 >> 3
    val v3 = commitCount / 100
    // 345 >> 45
    val v4 = commitCount % 100
    // 1.0.3.45
    val name = "$baseVerName.$v3.$v4"
    println("VerInfo-buildVerInfo :$code , $name")
    return Pair(code, name)
}

fun String.exec(): String = Runtime.getRuntime().exec(this)
    .inputStream.readBytes().toString(Charset.defaultCharset()).trim()