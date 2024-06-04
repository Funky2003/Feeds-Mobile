plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.feeds"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.feeds"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

//noinspection UseTomlInstead
dependencies {
    // data store
    implementation(group = "pl.droidsonroids.gif", name = "android-gif-drawable", version = "1.2.28")

    // material theme from google
    runtimeOnly(group = "com.google.android.material", name = "material", version = "1.13.0-alpha02")

    // supabase implementation
    runtimeOnly(group = "io.ktor", name = "ktor-client-okhttp-jvm", version = "2.3.11")
    runtimeOnly(group = "io.ktor", name = "ktor-client-websockets-jvm", version = "2.3.11")
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.12.0")
    implementation(platform("io.github.jan-tennert.supabase:bom:2.4.2"))
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.4.2")
    implementation("io.github.jan-tennert.supabase:storage-kt:2.4.2")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.4.2")
    implementation("io.ktor:ktor-client-android:2.3.11")
    implementation("io.ktor:ktor-client-core:2.3.11")
    implementation("io.ktor:ktor-utils:2.3.11")

    //noinspection UseTomlInstead
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    //The retrofit implementation
    runtimeOnly(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version = "1.9.0-RC")
    runtimeOnly(group = "androidx.lifecycle", name = "lifecycle-extensions", version = "2.2.0")
    implementation(group = "com.squareup.retrofit2", name = "retrofit", version = "2.9.0")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.10.1" )
    implementation(group = "com.squareup.retrofit2", name = "converter-gson", version = "2.9.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}