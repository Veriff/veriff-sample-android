package com.veriff.demo.dataSources

interface TokenDataSourceI {

    fun getToken(callback: Callback)


    interface Callback {
        fun gotToken(token: String)
        fun error(msg: String)
    }

}