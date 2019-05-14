package com.veriff.demo.data.dataSources.login

import com.veriff.demo.AppStatics.Companion.PREF_ACCESS_TOKEN
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.dataSources.DataSourceCallback
import com.veriff.demo.service.AppNetworkService
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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getCurrentAccessToken(): String {
        return localStorage.getString(PREF_ACCESS_TOKEN, "")
    }
}