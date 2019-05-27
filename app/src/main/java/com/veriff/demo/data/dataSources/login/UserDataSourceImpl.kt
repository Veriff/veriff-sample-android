package com.veriff.demo.data.dataSources.login

import com.veriff.demo.AppConfig
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.dataSources.DataSourceCallback
import com.veriff.demo.data.dataSources.getErrorDescription
import com.veriff.demo.data.dataSources.toMillis
import com.veriff.demo.service.AppNetworkService
import com.veriff.demo.utils.GeneralUtils
import com.veriff.demo.utils.localStorage.LocalStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserDataSourceImpl(private val appNetworkService: AppNetworkService,
                         private val localStorage: LocalStorage) : UserDataSource {

    override fun login(email: String, password: String, callback: DataSourceCallback) {
        val call = appNetworkService.login(email, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.accessToken?.let { token ->
                            localStorage.saveString(AppConfig.PREF_ACCESS_TOKEN, token)
                        }
                        it.refreshToken?.let { token ->
                            localStorage.saveString(AppConfig.PREF_REFRESH_TOKEN, token)
                        }
                        it.expiresIn?.let { expires ->
                            localStorage.saveLong(AppConfig.PREF_EXPIRES_IN, expires.toMillis())
                        }
                        localStorage.saveLong(AppConfig.PREF_LAST_LOGGED_IN, GeneralUtils.getCurrMillis())
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
        return localStorage.getString(AppConfig.PREF_ACCESS_TOKEN, "")
    }


    override fun getRefreshToken(): String {
        return localStorage.getString(AppConfig.PREF_REFRESH_TOKEN, "")
    }

    override fun getLastLoggedInMillis(): Long {
        return localStorage.getLong(AppConfig.PREF_LAST_LOGGED_IN, 0L)
    }

    override fun getExpiresIn(): Long {
        return localStorage.getLong(AppConfig.PREF_EXPIRES_IN, 0L)
    }
}




