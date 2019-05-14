package com.veriff.demo.utils.localStorage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefLocalStorage(context: Context, name: String) : LocalStorageI {

    private var sharedPrefs: SharedPreferences = context.getSharedPreferences(name,
            Context.MODE_PRIVATE)

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

    override fun saveObject(key: String, obj: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getObject(key: String): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}