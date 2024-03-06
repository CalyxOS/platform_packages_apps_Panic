/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    // https://android.googlesource.com/platform/external/kotlinc/+/refs/tags/android-14.0.0_r29/build.txt
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
}
