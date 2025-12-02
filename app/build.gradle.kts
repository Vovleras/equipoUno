plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.ksp)
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
  id("com.google.dagger.hilt.android")
  id("com.google.gms.google-services")
  id("jacoco")
}

android {
  namespace = "com.moviles.proyecto1"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.moviles.proyecto1"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  testOptions {
    unitTests {
      isReturnDefaultValues = true
    }
  }

  buildTypes {
    debug {
      enableUnitTestCoverage = true
    }
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
  testImplementation(libs.junit.junit)
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
  implementation("androidx.activity:activity-ktx:1.9.3")
  implementation("androidx.fragment:fragment-ktx:1.6.2")

  // LiveData
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

  // Room

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

  //dagger hilt
  implementation("com.google.dagger:hilt-android:2.47")
  kapt("com.google.dagger:hilt-android-compiler:2.47")

  //testing
  testImplementation("junit:junit:4.13.2")
  testImplementation("org.mockito:mockito-core:5.8.0")
  testImplementation("org.mockito:mockito-inline:5.2.0")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
  testImplementation("androidx.arch.core:core-testing:2.2.0")
  debugImplementation("org.jacoco:org.jacoco.core:0.8.7")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}

// Configuración de JaCoCo para cobertura de código
tasks.register<JacocoReport>("jacocoTestReport") {
  dependsOn("testDebugUnitTest")

  reports {
    xml.required.set(true)
    html.required.set(true)
    csv.required.set(false)
  }

  val fileFilter = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    "**/databinding/**/*.*",
    "**/hilt_aggregated_deps/**/*.*",
    "**/*_Factory.class",
    "**/*_MembersInjector.class",
    "**/*Module.class",
    "**/*Dagger*.*",
    "**/*Hilt*.*"
  )

  val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
    exclude(fileFilter)
  }

  val mainSrc = "${project.projectDir}/src/main/java"

  sourceDirectories.setFrom(files(mainSrc))
  classDirectories.setFrom(files(debugTree))
  executionData.setFrom(fileTree(project.buildDir) {
    include("jacoco/testDebugUnitTest.exec")
  })
}


