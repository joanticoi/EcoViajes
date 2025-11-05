import org.jetbrains.kotlin.fir.references.builder.buildImplicitThisReference
import org.jetbrains.kotlin.gradle.utils.IMPLEMENTATION

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "com.example.ecoviajes"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ecoviajes"
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
        compose = true
    }
}

dependencies {

    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
    implementation("androidx.work:work-runtime:2.10.5")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //fire base
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    //kotlix-coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    //viewmodel-lifecycle-compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    //Navigation compose
    implementation("androidx.navigation:navigation-compose:2.7.6")
    //material-Icons-extended
    implementation("androidx.compose.material:material-icons-extended:1.7.7")

    // Mostrar imágenes (para la vista previa de galería/cámara)
    implementation("io.coil-kt:coil-compose:2.6.0")
    // snapshotFlow y corrutinas base
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

}