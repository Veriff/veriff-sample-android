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

    override fun onSignInClicked() {
        if (loginModel.isLoggedIn()) {
            view.showLogoutConfirmation()

        } else {
            view.navigateToLogin()
        }
    }

    override fun onLogoutConfirmed() {
        loginModel.logout()
        view.setLoggedOutView()
        view.showToast(stringFetcher.run { getString(R.string.signed_out) })
    }

    override fun onLogoutCancelled() {}

    override fun startVeriffFlow() {
        makeTokenRequest()
    }


    override fun cancel() {}

}