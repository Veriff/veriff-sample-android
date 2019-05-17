package com.veriff.demo.data.dataSources

import okhttp3.ResponseBody
import org.json.JSONObject

internal fun ResponseBody.getErrorDescription(callback: DataSourceCallback) {
    val errorJsonObject = JSONObject(this.string())
    callback.error(errorJsonObject.getString("error_description"))
}

internal fun Long.toMillis(): Long {
    return this * 1000
}