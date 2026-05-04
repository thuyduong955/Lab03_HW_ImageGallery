plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.homework"
    // Sử dụng SDK 36 theo yêu cầu của các thư viện Core-Ktx mới
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.homework"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    
    // ML Kit & Image Loading
    implementation("com.google.mlkit:image-labeling:17.0.7")
    implementation("io.coil-kt:coil:2.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")
}