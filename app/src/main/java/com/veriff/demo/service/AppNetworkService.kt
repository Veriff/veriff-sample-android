package com.veriff.demo.service

import com.veriff.demo.AppConfig
import com.veriff.demo.AppConfig.API_CLIENT_ID
import com.veriff.demo.data.LoginResponse
import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import retrofit2.Call
import retrofit2.http.*

interface AppNetworkService {

    @Headers("X-AUTH-CLIENT:$API_CLIENT_ID", "CONTENT-TYPE:application/json")
    @POST("/v1/sessions")
    fun getToken(@Header("X-SIGNATURE") signature: String, @Body payload: TokenPayload): Call<TokenResponse>

    @POST("/v1/sessions")
    fun getTokenForUser(@Header("X-SIGNATURE") signature: String, @Body payload: TokenPayload,
                        @HeaderMap extraHeaders: Map<String, String>): Call<TokenResponse>

    @Headers("CONTENT-TYPE:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/v1/token")
    fun login(@Field("username") username: String, @Field("password") password: String,
              @Field("grant_type") grantType: String = "password"): Call<LoginResponse>
}