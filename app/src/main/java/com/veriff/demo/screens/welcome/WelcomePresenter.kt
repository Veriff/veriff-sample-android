package com.veriff.demo.screens.welcome

import com.veriff.demo.R
import com.veriff.demo.screens.login.LoginModel
import com.veriff.demo.utils.stringFetcher.StringFetcherI

class WelcomePresenter(private val view: WelcomeMVP.View, model: WelcomeModel,
                       private val loginModel: LoginModel,
                       private val stringFetcher: StringFetcherI) :
        WelcomeMVP.Presenter(view, model) {


    override fun start() {
        if (loginModel.isLoggedIn()) {
            view.setLoggedInView()
        } else {
            view.setLoggedOutView()
        }
    }

    override fun cancel() {

    }

    override fun onSignInClicked() {
        if (loginModel.isLoggedIn()) {
            loginModel.logout()
            view.setLoggedOutView()
            view.showToast(stringFetcher.getString(R.string.str_signed_out))
        } else {
            view.navigateToLogin()
        }
    }

    override fun startVeriffFlow() {
        makeTokenRequest()
    }

}