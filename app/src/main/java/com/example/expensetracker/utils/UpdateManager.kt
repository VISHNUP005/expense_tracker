package com.example.expensetracker.utils

import android.app.Activity
import android.content.IntentSender
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class UpdateManager(private val activity: Activity) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)

    fun checkForUpdates(
        onUpdateAvailable: () -> Unit,
        onNoUpdate: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Update is available
                onUpdateAvailable()
            } else {
                // No update available
                onNoUpdate()
            }
        }.addOnFailureListener { exception ->
            onError(exception)
        }
    }

    fun startImmediateUpdate(
        updateLauncher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        updateOptions,
                        100 // Request code
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun startFlexibleUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        updateOptions,
                        101 // Request code
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun completeFlexibleUpdate() {
        appUpdateManager.completeUpdate()
    }
}
