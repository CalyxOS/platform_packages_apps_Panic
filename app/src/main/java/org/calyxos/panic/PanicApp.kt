/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.HiltAndroidApp
import org.calyxos.panic.utils.PackageManagerReceiver

@HiltAndroidApp(Application::class)
class PanicApp : Hilt_PanicApp() {

    override fun onCreate() {
        super.onCreate()

        // Register broadcast receiver for package manager
        val pkgManagerBR = object : PackageManagerReceiver() {}
        registerReceiver(
            pkgManagerBR,
            IntentFilter().apply {
                addDataScheme("package")
                addAction(Intent.ACTION_PACKAGE_REMOVED)
            }
        )
    }
}
