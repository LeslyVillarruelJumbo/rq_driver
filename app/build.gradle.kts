plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Google Services
    alias(libs.plugins.gms.google.services)
}

android {
    namespace = "ec.edu.epn.rq_driver"
    compileSdk = 35

    defaultConfig {
        applicationId = "ec.edu.epn.rq_driver"
        minSdk = 25
        targetSdk = 35
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navegación
    implementation(libs.androidx.navigation.compose)

    // Enhanced UI
    implementation(libs.androidx.material.icons.extended)

    // Google Maps - APIs y Servicios
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)

    // Dependencias adicionales (por ejemplo, Material Icons)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.ui) // Verifica que la versión sea la correcta
    implementation(libs.material3)
    implementation(libs.androidx.foundation.layout.android) // Si usas Material3
    implementation(libs.androidx.fragment.ktx)

    // APIs
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

    // Google Play Services
    implementation(libs.play.services.base)
    implementation(libs.play.services.auth)
}
