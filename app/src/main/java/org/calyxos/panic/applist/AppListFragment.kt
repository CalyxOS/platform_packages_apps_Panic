/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.applist

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
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
import org.calyxos.panic.main.MainActivityViewModel
import org.calyxos.panic.utils.CommonUtils
import javax.inject.Inject

@AndroidEntryPoint(Fragment::class)
class AppListFragment :
    Hilt_AppListFragment(R.layout.fragment_app_list),
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var appListAdapterFactory: AppListRVAdapter.AppListAdapterFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var appListRVAdapter: AppListRVAdapter
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appListRVAdapter = appListAdapterFactory.getAdapter(R.id.appListFragment)

        // Recycler View
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appList.collect {
                    appListRVAdapter.submitList(it)
                }
            }
        }
        view.findViewById<RecyclerView>(R.id.recyclerView).adapter = appListRVAdapter
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        // Floating Action Button
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            viewModel.savePanicAppList(appListRVAdapter.currentList)
            findNavController().navigateUp()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == CommonUtils.panicAppListKey) {
            appListRVAdapter.submitList(viewModel.getAppList())
        }
    }
}
