package com.veriff.demo.data.dataSources

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

internal fun ResponseBody.getErrorDescription(callback: DataSourceCallback) {
    try {
        val errorJsonObject = JSONObject(this.string())
        callback.error(errorJsonObject.getString("error_description"))
    } catch (ex: JSONException) {
        callback.error("Internal error")
    }
}

internal fun Long.toMillis(): Long {
    return this * 1000
}