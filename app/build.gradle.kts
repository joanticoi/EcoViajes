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
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
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
    implementation(libs.androidx.compose.ui)
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
    implementation("androidx.fragment:fragment-ktx:1.8.5")

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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.google.firebase:firebase-storage-ktx")


    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Kotest para un estilo de testing BDD (Behavior-Driven Development)
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // MockK para crear "mocks" (objetos falsos) en tus tests
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.mockk:mockk-android:1.13.8")

    // Coroutines para pruebas (necesario por usar Dispatchers.setMain)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

        testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
        testImplementation("io.kotest:kotest-assertions-core:5.8.0")

        // ⚠️ ESTA ES LA QUE FALTA para que funcione 'checkAll' y 'Arb'
        testImplementation("io.kotest:kotest-property:5.8.0")

        testImplementation("io.mockk:mockk:1.13.9")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")
    testImplementation("io.kotest:kotest-property:5.9.0") // Necesario para 'checkAll' y 'Arb'

    // --- MOCKK (Para mockk, every, verify) ---
    testImplementation("io.mockk:mockk:1.13.10")
    androidTestImplementation("io.mockk:mockk-android:1.13.10") // Si corres tests en dispositivo

    // --- COROUTINES TEST (Para runTest, advanceUntilIdle) ---
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")

    // --- COMPOSE TESTING (Para createComposeRule en tests unitarios locales) ---
    // Esto permite correr tests de UI básicos sin emulador (usando Robolectric)
    testImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")
    testImplementation("org.robolectric:robolectric:4.12.1")

    }

