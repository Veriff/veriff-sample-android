package com.veriff.demo.data.dataSources.login

import com.veriff.demo.AppStatics.Companion.PREF_ACCESS_TOKEN
import com.veriff.demo.AppStatics.Companion.PREF_EXPIERES_IN
import com.veriff.demo.AppStatics.Companion.PREF_LAST_LOGGED_IN
import com.veriff.demo.AppStatics.Companion.PREF_REFRESH_TOKEN
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.dataSources.DataSourceCallback
import com.veriff.demo.data.dataSources.getErrorDescription
import com.veriff.demo.data.dataSources.toMillis
import com.veriff.demo.service.AppNetworkService
import com.veriff.demo.utils.GeneralUtils
import com.veriff.demo.utils.localStorage.LocalStorageI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserDataSource(private val appNetworkService: AppNetworkService,
                     private val localStorage: LocalStorageI) : UserDataSourceI {

    override fun login(email: String, password: String, callback: DataSourceCallback) {
        val call = appNetworkService.login(email, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        localStorage.saveString(PREF_ACCESS_TOKEN, it.accessToken)
                        localStorage.saveString(PREF_REFRESH_TOKEN, it.refreshToken)
                        localStorage.saveLong(PREF_EXPIERES_IN, it.expiresIn.toMillis())
                        localStorage.saveLong(PREF_LAST_LOGGED_IN, GeneralUtils.getCurrMillis())
                        callback.gotData(it)
                    }
                } else {
                    response.errorBody()?.getErrorDescription(callback)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.message?.let { callback.error(it) }
            }
        })
    }

    override fun logout() {
        localStorage.clearAll()
    }

    override fun getAccessToken(): String {
        return localStorage.getString(PREF_ACCESS_TOKEN, "")
    }


    override fun getRefreshToken(): String {
        return localStorage.getString(PREF_REFRESH_TOKEN, "")
    }

    override fun getLastLoggedInMillis(): Long {
        return localStorage.getLong(PREF_LAST_LOGGED_IN, 0L)
    }

    override fun getExpiresIn(): Long {
        return localStorage.getLong(PREF_EXPIERES_IN, 0L)
    }
}




