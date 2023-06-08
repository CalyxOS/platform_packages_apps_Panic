/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import info.guardianproject.panic.PanicResponder
import org.calyxos.panic.R

@AndroidEntryPoint(AppCompatActivity::class)
class MainActivity : Hilt_MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // Show confirmation dialog if triggering package is a new one
        val intentReferer: String? = PanicResponder.getConnectIntentSender(this)
        val triggerPkgName = PanicResponder.getTriggerPackageName(this)

        intentReferer?.let {
            if (it.isNotBlank() && intentReferer != triggerPkgName) {
                navHostFragment.navController.navigate(R.id.confirmationDialogFragment)
            }
        }
    }
}
