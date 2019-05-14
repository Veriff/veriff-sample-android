package com.veriff.demo.screens.login

import com.veriff.demo.R
import com.veriff.demo.data.dataSources.ModelCallback
import com.veriff.demo.utils.stringFetcher.StringFetcherI

class LoginPresenter(private val view: LoginMVP.View, model: LoginModel,
                     private val stringFetcher: StringFetcherI,
                     private val loginModel: LoginModel) : LoginMVP.Presenter(view, model) {


    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startVeriffFlow() {
        makeTokenRequest()
    }

    override fun login(email: String, password: String) {
        view.showProgress()
        var allFieldsValid = true
        if (email.isEmpty()) {
            view.showEmailError(stringFetcher.getString(R.string.err_invalid_email))
            allFieldsValid = false
        }

        if (password.isEmpty()) {
            view.showPasswordError(stringFetcher.getString(R.string.err_invalid_password))
            allFieldsValid = false
        }


        if (allFieldsValid) {
            loginModel.login(email, password, callback = object : ModelCallback {
                override fun gotData(data: Any?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun error(msg: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        } else {
            view.stopProgress()
        }

    }

}