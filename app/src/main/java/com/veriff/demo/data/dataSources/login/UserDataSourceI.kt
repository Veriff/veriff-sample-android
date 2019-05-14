package com.veriff.demo.data.dataSources.login

import com.veriff.demo.data.dataSources.DataSourceCallback

interface UserDataSourceI {
    fun login(email: String, password: String, callback: DataSourceCallback)
    fun getCurrentAccessToken(): String
}