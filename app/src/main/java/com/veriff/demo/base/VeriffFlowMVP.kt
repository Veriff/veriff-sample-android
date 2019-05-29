package com.veriff.demo.base

import com.veriff.demo.AppConfig
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSource.Callback

interface VeriffFlowMVP : BaseMVP {


    interface View : BaseMVP.BaseView {
        fun startVeriffFlow(sessionToken: String, url: String)
    }

    abstract class Presenter(private val view: View, private val model: VeriffFlowModel) : BaseMVP.BasePresenter {

        internal var sessionToken: String? = null
        internal var baseUrl = AppConfig.BASE_URL

        abstract fun startVeriffFlow()

        fun makeTokenRequestForUser(accessToken: String) {
            model.getSessionTokenForUser(accessToken, object : Callback {
                override fun gotToken(token: String) {
                    view.startVeriffFlow(token, baseUrl)
                }

                override fun error(msg: String) {
                    view.showToast(msg)
                }
            })
        }

        fun makeTokenRequest() {
            model.getSessionToken(object : Callback {
                override fun gotToken(token: String) {
                    view.startVeriffFlow(token, baseUrl)
                }

                override fun error(msg: String) {
                    view.showToast(msg)
                }
            })
        }
    }


}