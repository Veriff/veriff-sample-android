package com.veriff.demo.base

interface BaseMVP {

    interface BaseView {
        fun showToast(msg: String)
    }

    interface BasePresenter

}