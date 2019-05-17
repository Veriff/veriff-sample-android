package com.veriff.demo.base

import com.veriff.demo.AppStatics
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSourceI.Callback

interface VeriffFlowMVP : BaseMVP {


    interface View : BaseMVP.BaseView {
        fun startVeriffFlow(sessionToken: String, url: String)
    }

    abstract class Presenter(private val view: View, private val model: VeriffFlowModel) : BaseMVP.BasePresenter {

        private var sessionToken: String? = null
        internal var baseUrl = AppStatics.URL_STAGING

        abstract fun startVeriffFlow()

        fun parseQrCodeContents(contents: String): Pair<String?, String?> {
            val parsedContents = model.parseQrCodeContents(contents)
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