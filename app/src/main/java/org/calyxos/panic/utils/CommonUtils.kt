/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.core.graphics.drawable.toBitmap
import org.calyxos.panic.models.App

object CommonUtils {

    const val panicAppListKey = "panicAppList"

    fun getAllPackages(context: Context): List<App> {
        val applicationList = mutableListOf<App>()
        val packageManager = context.packageManager

        val packageList = packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        val validPackages = mutableListOf<PackageInfo>()

        packageList.forEach {
            val appInfo = it.applicationInfo
            if ((
                appInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 ||
                    appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
                ) && appInfo.enabled &&
                packageManager.getLaunchIntentForPackage(appInfo.packageName) != null &&
                it.packageName != context.packageName
            ) {
                validPackages.add(it)
            }
        }

        validPackages.forEach { packageInfo ->
            val app = App(
                packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                packageInfo.packageName,
                packageInfo.applicationInfo.loadIcon(packageManager).toBitmap(96, 96)
            )
            applicationList.add(app)
        }
        applicationList.sortBy { it.name }
        return applicationList.toList()
    }
}
