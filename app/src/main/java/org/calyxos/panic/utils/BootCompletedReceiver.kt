/*
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package org.calyxos.panic.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import dagger.hilt.android.AndroidEntryPoint
import lineageos.providers.LineageSettings
import org.calyxos.panic.R
import javax.inject.Inject

@AndroidEntryPoint(BroadcastReceiver::class)
class BootCompletedReceiver : Hilt_BootCompletedReceiver() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val panicChannelID = "panic"
    private val oneShotMigrationNotificationID = 100
    private val oneShotMigrationNotification = "oneShotMigrationNotification"

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (context != null && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Check and post migration notification if required
            handleOneShotNotification(context)
        }
    }

    private fun handleOneShotNotification(context: Context) {
        if (!sharedPreferences.getBoolean(oneShotMigrationNotification, false)) {

            // Only notify if panic option in power menu is turned on
            if (LineageSettings.Secure.getInt(
                    context.contentResolver,
                    LineageSettings.Secure.PANIC_IN_POWER_MENU,
                    0
                ) != 0
            ) {
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                createNotificationChannel(context, notificationManager)
                notificationManager.notify(oneShotMigrationNotificationID, getNotification(context))
            }
            sharedPreferences.edit { putBoolean(oneShotMigrationNotification, true) }
        }
    }

    private fun getNotification(context: Context): Notification {
        val intent = Intent().apply {
            component = ComponentName(
                "org.calyxos.ripple",
                "org.calyxos.ripple.SettingsActivityLink"
            )
        }
        val contentIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, panicChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.discover_panic_title))
            .setContentText(context.getString(R.string.discover_panic_desc))
            .setContentIntent(contentIntent)
            .setCategory(NotificationCompat.CATEGORY_SYSTEM)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel(
        context: Context,
        notificationManager: NotificationManager,
    ) {
        val defaultPriorityNotificationChannel = NotificationChannel(
            panicChannelID,
            context.getString(R.string.panic_channel_title),
            NotificationManager.IMPORTANCE_DEFAULT
        ).also {
            it.description = context.getString(R.string.panic_channel_desc)
        }

        // Create notification group and related channels
        notificationManager.createNotificationChannel(defaultPriorityNotificationChannel)
    }
}
