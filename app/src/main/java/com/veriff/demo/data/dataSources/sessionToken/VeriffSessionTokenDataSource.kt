package com.veriff.demo.data.dataSources.sessionToken

import com.google.gson.Gson
import com.veriff.demo.AppStatics
import com.veriff.demo.BuildConfig
import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import com.veriff.demo.service.AppNetworkService
import com.veriff.demo.utils.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VeriffSessionTokenDataSource(private val appNetworkService: AppNetworkService,
                                   private val gson: Gson) : SessionTokenDataSourceI {


    private val payloadEst = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"et\",\"features\":[\"selfid\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}"

    private val payloadEng = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"en\",\"features\":[\"selfid\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}"

    override fun getToken(callback: SessionTokenDataSourceI.Callback) {
        val load = gson.fromJson(payloadEst, TokenPayload::class.java)
        val toBeHashed = gson.toJson(load) + BuildConfig.API_SECRET
        val signature = GeneralUtils.sha256(toBeHashed)
        appNetworkService.getToken(signature, load).enqueue(object : Callback<TokenResponse> {
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

    override fun getTokenForUser(accessToken: String, callback: SessionTokenDataSourceI.Callback) {
        val load = gson.fromJson(payloadEng, TokenPayload::class.java)
        val toBeHashed = gson.toJson(load) + BuildConfig.API_SECRET
        val signature = GeneralUtils.sha256(toBeHashed)
        val headersMap = mapOf(
                "X-AUTH-CLIENT" to AppStatics.API_CLIENT_ID,
                "CONTENT-TYPE" to "application/json",
                "Authorization" to "bearer $accessToken"
        )
        appNetworkService.getTokenForUser(signature, load, headersMap).enqueue(object : Callback<TokenResponse> {
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