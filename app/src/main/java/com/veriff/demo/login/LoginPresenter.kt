package com.veriff.demo.login

import com.veriff.demo.AppStatics
import com.veriff.demo.BuildConfig
import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import com.veriff.demo.service.TokenService
import com.veriff.demo.utils.GeneralUtils
import mobi.lab.veriff.util.LogAccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private val view: LoginMVP.View, private val log: LogAccess,
                     private val tokenService: TokenService) : LoginMVP.Presenter {


    override fun startVeriffFlow() {
        makeTokenRequest()
    }

    private fun makeTokenRequest() {
        val pay = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"et\",\"features\":[\"selfid\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}"
        val load = AppStatics.GSON.fromJson(pay, TokenPayload::class.java)
        val toBeHashed = AppStatics.GSON.toJson(load) + BuildConfig.API_SECRET
        val signature = GeneralUtils.sha256(toBeHashed)

        tokenService.getToken(signature, load).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful && response.code() == AppStatics.REQUEST_SUCCESSFUL && response.body() != null) {
                    view.startVeriffFlow(response.body()!!.verification.sessionToken)
                } else {
                    view.showToast(response.message())
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                view.showToast("Something went wrong, please contact development team")
            }
        })
    }

}