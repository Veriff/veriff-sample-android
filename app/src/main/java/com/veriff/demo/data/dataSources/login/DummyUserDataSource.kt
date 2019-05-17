package com.veriff.demo.data.dataSources.login

import android.os.Handler
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.dataSources.DataSourceCallback

class DummyUserDataSource : UserDataSourceI {

    override fun login(email: String, password: String, callback: DataSourceCallback) {
        Handler().postDelayed({
            callback.gotData(LoginResponse(
                    accessToken = "1233434",
                    refreshToken = "",
                    expiresIn = 3000L
            ))
        }, 2000)
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccessToken(): String {
        return ""
    }

    override fun getLastLoggedInMillis(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRefreshToken(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getExpiresIn(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}