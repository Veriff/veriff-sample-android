package com.veriff.demo.screens.welcome

import com.veriff.demo.dataSources.TokenDataSourceI

class WelcomePresenter(view: WelcomeMVP.View, private val tokenDataSource: TokenDataSourceI) :
        WelcomeMVP.Presenter(view) {


    override fun startVeriffFlow() {
        makeTokenRequest(tokenDataSource)
    }
}