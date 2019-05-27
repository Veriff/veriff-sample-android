package com.veriff.demo.screens.login

import com.veriff.demo.base.VeriffFlowMVP

interface LoginMVP : VeriffFlowMVP {
    interface View : VeriffFlowMVP.View {
        fun showEmailError(msg: String)
        fun showPasswordError(msg: String)
        fun showLoginError(msg: String)
    }
}