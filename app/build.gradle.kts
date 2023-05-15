plugins {
//    id ("com.android.application")
//    id ("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.notescleanarchitecture"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.notescleanarchitecture"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphic)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(platform(libs.androidx.compose.bom))

    //activity xml
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)

//    implementation ("androidx.core:core-ktx:1.8.0")
//    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
//    implementation ("androidx.activity:activity-compose:1.5.1")
//    implementation ("androidx.compose.ui:ui")
//    implementation ("androidx.compose.ui:ui-graphics")
//    implementation ("androidx.compose.ui:ui-tooling-preview")
//    implementation ("androidx.compose.material3:material3")
//    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
//
//    testImplementation ("junit:junit:4.13.2")
//    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation ("androidx.compose.ui:ui-test-junit4")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
//    debugImplementation ("androidx.compose.ui:ui-tooling")
//    debugImplementation ("androidx.compose.ui:ui-test-manifest")
}