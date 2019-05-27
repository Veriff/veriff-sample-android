package com.veriff.demo.screens.login

import com.veriff.demo.base.VeriffFlowModel
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.dataSources.DataSourceCallback
import com.veriff.demo.data.dataSources.ModelCallback
import com.veriff.demo.data.dataSources.login.UserDataSource
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSource
import com.veriff.demo.utils.GeneralUtils

class LoginModel(sessionTokenDataSource: SessionTokenDataSource,
                 private val userDataSource: UserDataSource)
    : VeriffFlowModel(sessionTokenDataSource) {


    fun login(email: String, password: String, callback: ModelCallback) {
        userDataSource.login(email, password, callback = object : DataSourceCallback {
            override fun gotData(data: Any?) {
                (data as LoginResponse).let {
                    callback.gotData(data)
                }
            }

            override fun error(msg: String) {
                callback.error(msg)
            }
        })
    }


    fun isLoggedIn(): Boolean {
        val currMillis = GeneralUtils.getCurrMillis()
        val lastLoggedInMillis = userDataSource.getLastLoggedInMillis()
        val expiresIn = userDataSource.getExpiresIn()
        val accessToken = userDataSource.getAccessToken()
        if ((currMillis - lastLoggedInMillis) < expiresIn && accessToken.isNotEmpty()) {
            return true
        }
        return false
    }

    fun logout() {
        userDataSource.logout()
    }

}