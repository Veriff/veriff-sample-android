package com.veriff.demo.screens.login

import com.veriff.demo.dataSources.TokenDataSourceI

class LoginPresenter(view: LoginMVP.View,
                     private val tokenDataSource: TokenDataSourceI) : LoginMVP.Presenter(view) {

    override fun startVeriffFlow() {
        makeTokenRequest(tokenDataSource)
    }

}