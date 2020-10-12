package com.example.test

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt

class AppAuthenticationCallback(
    private val context: Context,
    val onSucceeded: () -> Unit,
    val onError: () -> Unit
) : BiometricPrompt.AuthenticationCallback() {
    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        onError()
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        Toast.makeText(
            context,
            context.getString(R.string.msg_auth_succeeded), Toast.LENGTH_SHORT
        ).show()
        onSucceeded()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        Toast.makeText(
            context,
            context.getString(R.string.error_msg_auth_failed),
            Toast.LENGTH_SHORT
        ).show()
    }
}