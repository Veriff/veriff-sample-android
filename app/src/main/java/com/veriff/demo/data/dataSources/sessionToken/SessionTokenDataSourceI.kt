package com.veriff.demo.data.dataSources.sessionToken

interface SessionTokenDataSourceI {

    fun getToken(callback: Callback)
    fun getTokenForUser(accessToken: String, callback: Callback)

    interface Callback {
        fun gotToken(token: String)
        fun error(msg: String)
    }

}