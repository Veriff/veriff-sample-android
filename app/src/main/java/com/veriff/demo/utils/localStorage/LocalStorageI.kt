package com.veriff.demo.utils.localStorage

interface LocalStorageI {


    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defValue: Int): Int

    fun saveLong(key: String, value: Long)
    fun getLong(key: String, defValue: Long): Long

    fun saveFloat(key: String, value: Float)
    fun getFloat(key: String, defValue: Float): Float

    fun saveString(key: String, value: String)
    fun getString(key: String, defValue: String): String

    fun saveObject(key: String, obj: Any)
    fun getObject(key: String): Any?

}