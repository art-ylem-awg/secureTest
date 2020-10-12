package com.example.test.viewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.example.test.R
import java.util.concurrent.Executor

class EnterViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    lateinit var executor: Executor
    lateinit var biometricManager: BiometricManager

    var sharedPreferences =
        context.getSharedPreferences(context.getString(R.string.user_data), Context.MODE_PRIVATE)

    fun isCodeExist(exist: () -> Unit, notExist: () -> Unit) {
        if (sharedPreferences.contains(context.getString(R.string.code_key)) && !sharedPreferences.getString(
                context.getString(R.string.code_key),
                ""
            )
                .equals("")
        ) {
            exist()
        } else {
            notExist()
        }
    }

    fun getSharedPreferencesCode(): String {
        return if (!sharedPreferences.getString(context.getString(R.string.code_key), "")
                .equals("")
        ) {
            sharedPreferences.getString(context.getString(R.string.code_key), "").toString()
        } else {
            context.getString(R.string.enter_code)
        }
    }

    fun auth(success: () -> Unit, noneEnrolled: () -> Unit) {
        executor = ContextCompat.getMainExecutor(context)
        biometricManager = BiometricManager.from(context)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> success()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg_no_biometric_hardware),
                    Toast.LENGTH_LONG
                ).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg_biometric_hw_unavailable),
                    Toast.LENGTH_LONG
                ).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> noneEnrolled()
        }
    }

    fun initPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.auth_title))
            .setDeviceCredentialAllowed(false)
            .setConfirmationRequired(false)
            .setNegativeButtonText(context.getString(R.string.enter_pin_code))
            .build()
}