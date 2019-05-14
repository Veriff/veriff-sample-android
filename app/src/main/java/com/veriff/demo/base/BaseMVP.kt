package com.veriff.demo.base

interface BaseMVP {

    interface BaseView {
        fun showToast(msg: String)
        fun showProgress()
        fun stopProgress()
    }

    interface BasePresenter {
        fun start()
        fun cancel()
    }

}