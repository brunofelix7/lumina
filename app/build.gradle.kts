plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

android {
    namespace = "dev.brunofelix.lumina"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.brunofelix.lumina"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Modules
    implementation(project(":core:designsystem"))
    implementation(project(":core:presentation"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:splash"))

    // Jetpack Compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.compose)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    ksp(libs.hilt.android.compiler)

    // Firebase
    val firebaseBom = platform(libs.firebase.bom)
    implementation(firebaseBom)

    // Unit tests
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.mockk)

    // Instrumentation tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.kotest.assertions.core)
}
