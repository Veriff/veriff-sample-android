package com.veriff.demo.screens.login

import com.veriff.demo.base.VeriffFlowModel
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.dataSources.DataSourceCallback
import com.veriff.demo.data.dataSources.ModelCallback
import com.veriff.demo.data.dataSources.login.UserDataSourceI
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSourceI
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParserI

class LoginModel(qrCodeContentsParser: QrCodeContentsParserI,
                 sessionTokenDataSource: SessionTokenDataSourceI,
                 private val userDataSource: UserDataSourceI)
    : VeriffFlowModel(qrCodeContentsParser, sessionTokenDataSource) {


    fun login(email: String, password: String, callback: ModelCallback) {
        userDataSource.login(email, password, callback = object : DataSourceCallback{
            override fun gotData(data: Any?) {
                (data as LoginResponse).let {

                }
            }

            override fun error(msg: String) {
                callback.error(msg)
            }
        })
    }


    fun isLoggedIn(): Boolean{
        return userDataSource.getCurrentAccessToken().isNotEmpty()
    }

}