package com.veriff.demo.base

import com.veriff.demo.dataSources.TokenDataSourceI

interface VeriffFlowMVP : BaseMVP {


    interface View : BaseMVP.BaseView {
        fun startVeriffFlow(sessionToken: String)
    }

    abstract class Presenter(private val view: View) : BaseMVP.BasePresenter {
        abstract fun startVeriffFlow()

        fun makeTokenRequest(tokenDataSource: TokenDataSourceI) {
            tokenDataSource.getToken(object : TokenDataSourceI.Callback {
                override fun gotToken(token: String) {
                    view.startVeriffFlow(token)
                }
                override fun error(msg: String) {
                    view.showToast(msg)
                }
            })
        }
    }


}