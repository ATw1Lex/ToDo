plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.atwilex.to_do"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.atwilex.to_do"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        kapt {
            arguments {arg("room.schemaLocation", "$projectDir/schemas")}
        }
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // Kotlin Coroutines
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    implementation ("androidx.room:room-runtime:2.7.1") // Библиотека "Room"
    kapt ("androidx.room:room-compiler:2.7.1") // Кодогенератор
    implementation ("androidx.room:room-ktx:2.7.1") // Дополнительно для Kotlin Coroutines, Kotlin Flows
}