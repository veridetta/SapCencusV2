plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.sapcencuskotlin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sapcencuskotlin"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.intuit.sdp:sdp-android:1.0.6")
    //implementation("com.github.jkwiecien:EasyImage:3.2.0")
    implementation("com.google.mlkit:face-detection:16.0.0")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.8.0")
    implementation("com.squareup.retrofit2:converter-gson:2.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("com.github.marchinram:RxGallery:0.6.5")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation("com.jakewharton.timber:timber:4.7.1")

    //firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))

//    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.4.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-appcheck-debug:17.1.1")
    //appcheck

}