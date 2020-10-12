package com.example.test.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.test.R

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private var sharedPreferences =
        context.getSharedPreferences(context.getString(R.string.user_data), Context.MODE_PRIVATE)

    fun getSharedPreferencesCode(): String {
        return if (!sharedPreferences.getString(context.getString(R.string.code_key), "")
                .equals("")
        ) {
            sharedPreferences.getString(context.getString(R.string.code_key), "").toString()
        } else {
            context.getString(R.string.enter_code)
        }
    }

    fun editSharedPref(code: String) {
        val editor = sharedPreferences.edit()
        editor.putString(context.getString(R.string.code_key), code)
        editor.apply()
    }
}