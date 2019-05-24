package com.veriff.demo.base

import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSource
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSource.Callback

open class VeriffFlowModel(private val sessionTokenDataSource: SessionTokenDataSource) {

    fun getSessionToken(callback: Callback) {
        sessionTokenDataSource.getToken(callback)
    }

    fun getSessionTokenForUser(accessToken: String, callback: Callback) {
        sessionTokenDataSource.getTokenForUser(accessToken, callback)
    }

}