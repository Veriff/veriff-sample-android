package com.veriff.demo.data.dataSources

interface ModelCallback {

    fun gotData(data: Any?)
    fun error(msg: String)

}