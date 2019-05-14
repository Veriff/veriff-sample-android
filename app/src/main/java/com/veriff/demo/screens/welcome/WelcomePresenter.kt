package com.veriff.demo.screens.welcome

import com.veriff.demo.screens.login.LoginModel

class WelcomePresenter(view: WelcomeMVP.View, private val model: WelcomeModel,
                       private val loginModel: LoginModel) :
        WelcomeMVP.Presenter(view, model) {


    override fun start() {
        if (loginModel.isLoggedIn()){

        } else{

        }
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startVeriffFlow() {
        makeTokenRequest()
    }

}