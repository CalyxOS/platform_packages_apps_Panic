/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.calyxos.panic.R

@AndroidEntryPoint(AppCompatActivity::class)
class MainActivity : Hilt_MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
