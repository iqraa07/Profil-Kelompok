plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // WAJIB di Kotlin 2 + Compose
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    // ↑ versi ini harus sama dengan versi Kotlin yang dipakai di project-mu,
    // kalau di libs.versions.toml tertulis 2.0.21 ganti sesuai di sana
}

android {
    namespace = "com.example.test"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.test"
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    // DI KOTLIN 2: blok ini DIHAPUS / dikomen
    // composeOptions {
    //     kotlinCompilerExtensionVersion = "1.5.14"
    // }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment)

    // ===== Jetpack Compose (boleh abaikan warning "use version catalog") =====
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui-tooling-preview")

    debugImplementation("androidx.compose.ui:ui-tooling")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
