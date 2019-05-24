package com.veriff.demo.utils.localStorage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefLocalStorageImpl(context: Context, name: String) : LocalStorage {

    private var sharedPrefs: SharedPreferences = context.getSharedPreferences(name,
            Context.MODE_PRIVATE)

    override fun clearAll() {
        sharedPrefs
                .edit()
                .clear()
                .apply()
    }

    override fun saveInt(key: String, value: Int) {
        sharedPrefs
                .edit()
                .putInt(key, value)
                .apply()
    }

    override fun getInt(key: String, defValue: Int): Int {
        return sharedPrefs.getInt(key, defValue)
    }

    override fun saveLong(key: String, value: Long) {
        sharedPrefs
                .edit()
                .putLong(key, value)
                .apply()
    }

    override fun getLong(key: String, defValue: Long): Long {
        return sharedPrefs.getLong(key, defValue)
    }

    override fun saveFloat(key: String, value: Float) {
        sharedPrefs
                .edit()
                .putFloat(key, value)
                .apply()
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return sharedPrefs.getFloat(key, defValue)
    }

    override fun saveString(key: String, value: String) {
        sharedPrefs
                .edit()
                .putString(key, value)
                .apply()
    }

    override fun getString(key: String, defValue: String): String {
        return sharedPrefs.getString(key, defValue)
    }

}