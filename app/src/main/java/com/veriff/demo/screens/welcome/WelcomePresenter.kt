package com.veriff.demo.screens.welcome

import com.veriff.demo.R
import com.veriff.demo.screens.login.LoginModel
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParser
import com.veriff.demo.utils.stringFetcher.StringFetcher

class WelcomePresenter(private val view: WelcomeMVP.View, model: WelcomeModel,
                       private val qrCodeContentsParser: QrCodeContentsParser,
                       private val loginModel: LoginModel,
                       private val stringFetcher: StringFetcher) :
        WelcomeMVP.Presenter(view, model) {


    override fun start() {
        if (loginModel.isLoggedIn()) {
            view.setLoggedInView()
        } else {
            view.setLoggedOutView()
        }
    }

    override fun parseQrCodeContents(contents: String): Pair<String?, String?> {
        val parsedContents = qrCodeContentsParser.parseQrCodeContents(contents)
        parsedContents.first?.let {
            baseUrl = it
        }
        parsedContents.second?.let {
            sessionToken = it
        }

        sessionToken?.let {
            if (it.isNotEmpty()) {
                view.startVeriffFlow(it, baseUrl)
            } else {
                view.showToast("No token available, try again")
            }
        }

        return parsedContents
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