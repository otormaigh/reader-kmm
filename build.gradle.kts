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

buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:7.1.0-alpha10")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.21")
    classpath("com.squareup.sqldelight:gradle-plugin:1.5.1")
  }
}

group = "ie.otormaigh.reader"
version = "0.1"

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

tasks.withType<Wrapper> {
  gradleVersion = "7.2"
  distributionType = Wrapper.DistributionType.BIN
}