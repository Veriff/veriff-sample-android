package com.veriff.demo.data.dataSources.sessionToken

import com.google.gson.Gson
import com.veriff.demo.AppConfig
import com.veriff.demo.BuildConfig
import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import com.veriff.demo.service.AppNetworkService
import com.veriff.demo.utils.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VeriffSessionTokenDataSourceImpl(private val appNetworkService: AppNetworkService,
                                       private val gson: Gson) : SessionTokenDataSource {

    private val payload = TokenPayload(TokenPayload.Verification(
            TokenPayload.Verification.Person("Tundmatu", "Toomas",
                    "38508260269"), null))
    private val toBeHashed = gson.toJson(payload) + BuildConfig.API_SECRET
    private val signature = GeneralUtils.sha256(toBeHashed)

    override fun getToken(callback: SessionTokenDataSource.Callback) {
        appNetworkService.getToken(signature, payload).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful && response.code() == AppConfig.REQUEST_SUCCESSFUL && response.body() != null) {
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

    override fun getTokenForUser(accessToken: String, callback: SessionTokenDataSource.Callback) {
        val headersMap = mapOf(
                "X-AUTH-CLIENT" to AppConfig.API_CLIENT_ID,
                "CONTENT-TYPE" to "application/json",
                "Authorization" to "bearer $accessToken"
        )
        appNetworkService.getTokenForUser(signature, payload, headersMap).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful && response.code() == AppConfig.REQUEST_SUCCESSFUL && response.body() != null) {
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