/*
 * Copyright 2021 Elliot Tormey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  id("com.android.application")
  kotlin("android")
}

group = "ie.otormaigh.reader"
version = "0.1"

android {
  compileSdk = 30

  defaultConfig {
    applicationId = "ie.otormaigh.reader"
    minSdk = 24
    targetSdk = 30
    versionCode = 1
    versionName = "1.0"
    base.archivesName.set("reader-$versionName")
  }

  buildFeatures {
    viewBinding = true
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.0.1"
  }

  signingConfigs {
    named("debug").configure {
      storeFile = file("signing/debug.keystore")
    }
  }

  buildTypes {
    getByName("debug") {
      signingConfig = signingConfigs["debug"]
      applicationIdSuffix = ".debug"
    }

    getByName("release") {
      signingConfig = signingConfigs["debug"]
      postprocessing {
        proguardFiles.add(file("proguard-rules.pro"))
        isRemoveUnusedResources = true
        isRemoveUnusedCode = true
        isOptimizeCode = true
        isObfuscate = true
      }
    }
  }
}

dependencies {
  implementation(project(":shared"))

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1-native-mt")

  implementation("com.google.android.material:material:1.4.0")
  implementation("androidx.appcompat:appcompat:1.3.1")
  implementation("androidx.core:core-ktx:1.6.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.0")
  implementation("androidx.activity:activity-compose:1.3.1")
  implementation("androidx.compose.material:material:1.0.1")
  implementation("androidx.compose.ui:ui-tooling:1.0.1")

  implementation("com.jakewharton.timber:timber:5.0.1")
}
