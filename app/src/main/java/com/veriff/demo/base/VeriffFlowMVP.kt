package com.veriff.demo.base

import com.veriff.demo.AppConfig
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSource.Callback

interface VeriffFlowMVP : BaseMVP {


    interface View : BaseMVP.BaseView {
        fun startVeriffFlow(sessionToken: String, url: String)
        fun disableSessionGeneration()
        fun enableSessionGeneration()
    }

    abstract class Presenter(private val view: View, private val model: VeriffFlowModel) : BaseMVP.BasePresenter {

        internal var sessionToken: String? = null
        internal var baseUrl = AppConfig.BASE_URL

        abstract fun startVeriffFlow()

        fun makeTokenRequestForUser(accessToken: String) {
            view.disableSessionGeneration()
            model.getSessionTokenForUser(accessToken, object : Callback {
                override fun gotToken(token: String) {
                    view.startVeriffFlow(token, baseUrl)
                    view.enableSessionGeneration()
                }

                override fun error(msg: String) {
                    view.showToast(msg)
                    view.enableSessionGeneration()
                }
            })
        }

        fun makeTokenRequest() {
            view.disableSessionGeneration()
            model.getSessionToken(object : Callback {
                override fun gotToken(token: String) {
                    view.startVeriffFlow(token, baseUrl)
                    view.enableSessionGeneration()
                }

                override fun error(msg: String) {
                    view.showToast(msg)
                    view.enableSessionGeneration()
                }
            })
        }
    }


}