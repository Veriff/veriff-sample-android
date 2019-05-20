package com.veriff.demo.data.dataSources

interface DataSourceCallback {
    fun gotData(data: Any?)
    fun error(msg: String)
}