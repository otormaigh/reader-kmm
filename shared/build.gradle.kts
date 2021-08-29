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

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  kotlin("plugin.serialization")
  id("com.squareup.sqldelight")
}

group = "ie.otormaigh.reader"
version = "0.1"

kotlin {
  android()

  ios {
    binaries {
      framework {
        baseName = "shared"
      }
    }
    // https://github.com/cashapp/sqldelight/issues/2044#issuecomment-721299517
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) iosArm64("ios")
    else iosX64("ios")
  }

  val ktorVersion = "1.6.3"
  val sqlDelightVersion = "1.5.1"
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt") {
          version {
            strictly("1.5.1-native-mt")
          }
        }

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")

        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")

        implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val androidMain by getting {
      dependencies {
        implementation("com.google.android.material:material:1.4.0")
        implementation("io.ktor:ktor-client-android:$ktorVersion")

        implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
      }
    }
    val androidTest by getting {
      dependencies {
        implementation("junit:junit:4.13.2")
      }
    }

    val iosMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-ios:$ktorVersion")

        implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
      }
    }
    val iosTest by getting
  }
}

android {
  compileSdk = 30
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 24
    targetSdk = 30
  }
}

sqldelight {
  database("ReaderDatabase") {
    packageName = "ie.otormaigh.reader.shared.persistence"
    schemaOutputDirectory = file("src/commonMain/sqldelight/ie/otormaigh/reader/shared/persistence/databases")
    verifyMigrations = true
  }
}

tasks.getByName("preBuild").dependsOn(":shared:generateSqlDelightInterface")

val packForXcode by tasks.creating(Sync::class) {
  group = "build"
  val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
  val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
  val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
  val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
  inputs.property("mode", mode)
  dependsOn(framework.linkTask)
  val targetDir = File(buildDir, "xcode-frameworks")
  from({ framework.outputDirectory })
  into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)