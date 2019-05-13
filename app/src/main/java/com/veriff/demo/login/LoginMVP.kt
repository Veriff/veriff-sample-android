package com.veriff.demo.login

interface LoginMVP {


    interface View {
        fun startVeriffFlow(sessionToken: String)
        fun showToast(msg: String)
    }


    interface Presenter {
        fun startVeriffFlow()
    }
}