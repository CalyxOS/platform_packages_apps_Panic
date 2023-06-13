/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.calyxos.panic.R
import org.calyxos.panic.applist.AppListRVAdapter
import org.calyxos.panic.utils.CommonUtils.panicAppListKey
import javax.inject.Inject

@AndroidEntryPoint(Fragment::class)
class MainFragment :
    Hilt_MainFragment(R.layout.fragment_main),
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var appListAdapterFactory: AppListRVAdapter.AppListAdapterFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var appListRVAdapter: AppListRVAdapter
    private val viewModel: MainActivityViewModel by activityViewModels()

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

        // Recycler View
        appListRVAdapter = appListAdapterFactory.getAdapter()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appList.collect { list ->
                    appListRVAdapter.submitList(list.filter { it.panicApp })
                }
            }
        }
        view.findViewById<RecyclerView>(R.id.recyclerView).adapter = appListRVAdapter
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == panicAppListKey) {
            appListRVAdapter.submitList(viewModel.getAppList().filter { it.panicApp })
        }
    }
}
