package com.veriff.demo.screens.login

import com.veriff.demo.base.VeriffFlowMVP
import com.veriff.demo.base.VeriffFlowModel

interface LoginMVP : VeriffFlowMVP {
    interface View : VeriffFlowMVP.View {
        fun showEmailError(msg: String)
        fun showPasswordError(msg: String)
        fun showLoginError(msg: String)
    }

    abstract class Presenter(view: View, model: LoginModel)
        : VeriffFlowMVP.Presenter(view, model as VeriffFlowModel) {
        abstract fun login(email: String, password: String)
    }
}