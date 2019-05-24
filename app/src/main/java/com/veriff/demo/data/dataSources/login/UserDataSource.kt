package com.veriff.demo.data.dataSources.login

import com.veriff.demo.data.dataSources.DataSourceCallback

interface UserDataSource {
    fun login(email: String, password: String, callback: DataSourceCallback)
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun getExpiresIn(): Long
    fun getLastLoggedInMillis(): Long
    fun logout()
}