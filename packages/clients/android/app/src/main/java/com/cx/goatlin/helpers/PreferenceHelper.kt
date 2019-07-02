package com.cx.goatlin.helpers

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private lateinit var sharedPreferences: SharedPreferences

    public fun init(context: Context) {
            if (!this::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }
    }

    public fun getString(key: String, default: String?): String {
        return sharedPreferences.getString(key, default)
    }

    public fun getInt(key: String, default: Int = -1): Int {
        return sharedPreferences.getInt(key, default)
    }
}