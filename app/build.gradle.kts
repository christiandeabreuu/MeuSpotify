plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.spotify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.spotify"
        minSdk = 24
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
        viewBinding = true
        buildConfig = true
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

    //Room Database
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Coil (para carregar imagens)
    implementation(libs.coil)

    // Navigation
    implementation(libs.navigationFragmentKtx)
    implementation(libs.navigationUiKtx)

    // Coroutines
    implementation(libs.coroutines.android)

    // Lifecycle (para ViewModel e LiveData)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    // RecyclerView
    implementation(libs.recyclerview)

    //Koin
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.androidx.workmanager)

    // MockK para testes unitários
    testImplementation(libs.mockk)

    // MockK para testes instrumentados (Android)
    androidTestImplementation(libs.mockk.android)

    // Coroutines Test
    testImplementation(libs.kotlinx.coroutines.test)

    // Dependência para InstantTaskExecutorRule
    testImplementation(libs.androidx.arch.core.testing)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Dependências para requisições HTTP
    implementation(libs.okhttp)

    // Dependência para autenticação OAuth
    implementation(libs.spotify.auth)
    implementation(libs.browser)

    //pagination
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.runtime.ktx)

}