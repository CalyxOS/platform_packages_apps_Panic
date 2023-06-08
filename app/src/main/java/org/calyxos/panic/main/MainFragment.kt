/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import org.calyxos.panic.R

@AndroidEntryPoint(Fragment::class)
class MainFragment : Hilt_MainFragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        if (activity?.intent?.action == Intent.ACTION_MAIN) {
            toolbar.navigationIcon = null
        } else {
            toolbar.setNavigationOnClickListener { activity?.finish() }
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> findNavController().navigate(R.id.settingsFragment)
            }
            true
        }

        // Floating Action Button
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            findNavController().navigate(R.id.appListFragment)
        }
    }
}
