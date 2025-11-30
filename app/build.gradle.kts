plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
  id("com.google.gms.google-services")
}

android {
  namespace = "com.moviles.proyecto1"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.moviles.proyecto1"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    dataBinding = true
  }

}

dependencies {

  implementation("com.google.android.material:material:1.12.0")
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)
  val navVersion = "2.7.3"

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  // Navigation
  implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
  implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")
  implementation("androidx.navigation:navigation-common:${navVersion}")

  //cardView
  implementation("androidx.cardview:cardview:1.0.0")

  //RecyclerView
  implementation("androidx.recyclerview:recyclerview:1.3.1")

  //corrutinas
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

  //viewmodel
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
  implementation("androidx.activity:activity-ktx:1.8.0")
  implementation("androidx.fragment:fragment-ktx:1.6.2")

  // LiveData
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

  // Room
  implementation("androidx.room:room-runtime:2.5.2")
  implementation("androidx.room:room-ktx:2.5.2")
  ksp("androidx.room:room-compiler:2.5.2")
  implementation("com.getbase:floatingactionbutton:1.10.1")


  //Retrofit
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")

  //Glide
  implementation("com.github.bumptech.glide:glide:4.12.0")

  // Lottie (Json Animations)
  implementation("com.airbnb.android:lottie:6.4.0")

  // Biometric Authentication
  implementation("androidx.biometric:biometric:1.1.0")

  //firebase
  implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
  implementation("com.google.firebase:firebase-firestore")
  implementation("com.google.firebase:firebase-auth")
}