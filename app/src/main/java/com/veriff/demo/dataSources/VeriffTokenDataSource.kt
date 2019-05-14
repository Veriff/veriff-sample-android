package com.veriff.demo.dataSources

import com.veriff.demo.AppStatics
import com.veriff.demo.BuildConfig
import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import com.veriff.demo.service.TokenService
import com.veriff.demo.utils.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VeriffTokenDataSource(private val tokenService: TokenService): TokenDataSourceI {


    override fun getToken(callback: TokenDataSourceI.Callback) {
        val pay = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"et\",\"features\":[\"selfid\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}"
        val load = AppStatics.GSON.fromJson(pay, TokenPayload::class.java)
        val toBeHashed = AppStatics.GSON.toJson(load) + BuildConfig.API_SECRET
        val signature = GeneralUtils.sha256(toBeHashed)
        tokenService.getToken(signature, load).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful && response.code() == AppStatics.REQUEST_SUCCESSFUL && response.body() != null) {
                    response.body()?.verification?.sessionToken?.let { callback.gotToken(it) }
                } else {
                    callback.error(response.message())
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                callback.error("Something went wrong, please contact development team")
            }
        })
    }
}