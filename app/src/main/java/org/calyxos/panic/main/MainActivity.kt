/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import info.guardianproject.panic.Panic
import info.guardianproject.panic.PanicResponder
import org.calyxos.panic.R

@AndroidEntryPoint(AppCompatActivity::class)
class MainActivity : Hilt_MainActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (intent?.action) {
            Panic.ACTION_TRIGGER -> {
                val prefManager = PreferenceManager.getDefaultSharedPreferences(this)
                viewModel.uninstallPanicApps()
                if (prefManager.getBoolean("exit_app", true)) finish()
            }
            Panic.ACTION_DISCONNECT -> {
                PanicResponder.checkForDisconnectIntent(this)
                finish()
            }
        }
    }
}
