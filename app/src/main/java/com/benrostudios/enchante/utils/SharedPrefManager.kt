package com.benrostudios.enchante.utils

import android.content.Context

class SharedPrefManager(val context: Context) {
    private fun getPrefs() = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var jwtStored: String
        get() = getPrefs()?.getString(PREFS_JWT, "") ?: ""
        set(value) {
            getPrefs()?.edit()?.putString(PREFS_JWT, value)?.apply()
        }

    var username: String
        get() = getPrefs()?.getString(PREFS_USERNAME, "") ?: ""
        set(value) {
            getPrefs()?.edit()?.putString(PREFS_USERNAME, value)?.apply()
        }

    var userImageURL: String
        get() = getPrefs()?.getString(PREFS_PFP_URL, "") ?: ""
        set(value) {
            getPrefs()?.edit()?.putString(PREFS_PFP_URL, value)?.apply()
        }

    var userPhoneNumber: String
        get() = getPrefs()?.getString(PREFS_USER_PHONE, "") ?: ""
        set(value) {
            getPrefs()?.edit()?.putString(PREFS_USER_PHONE, value)?.apply()
        }

    fun nukeSharedPrefs() {
        getPrefs()?.edit()?.clear()?.apply()
    }

    companion object {
        const val PREFS_FILENAME = "com.benrostudios.enchante.prefs"
        const val PREFS_USERNAME = "username"
        const val PREFS_PFP_URL = "profileUrl"
        const val PREFS_USER_PHONE = "userPhone"
        const val PREFS_JWT = "jwt"
    }
}