plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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

    // Coil (para carregar imagens)
    implementation(libs.coil)

    // Navigation
    implementation(libs.navigationFragmentKtx)    // Fragment
    implementation(libs.navigationUiKtx)           // UI

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
//    implementation(libs.browser)

//    implementation 'com.spotify.android:spotify-auth:1.2.3' // Verifique a versão mais recente na documentação
//    implementation 'com.spotify.android:spotify-player:2.4.0' //  implementar
}