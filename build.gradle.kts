/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.0" apply false
    // https://android.googlesource.com/platform/external/kotlinc/+/refs/tags/android-15.0.0_r1/build.txt
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
}
