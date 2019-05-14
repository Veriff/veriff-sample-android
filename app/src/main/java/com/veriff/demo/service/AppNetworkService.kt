package com.veriff.demo.service

import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import com.veriff.demo.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AppNetworkService {

    @Headers("X-AUTH-CLIENT:24d887f4-ad3e-43da-bc98-c8099ad6f430", "CONTENT-TYPE:application/json")
    @POST("/v1/sessions")
    fun getToken(@Header("X-SIGNATURE") signature: String, @Body payload: TokenPayload): Call<TokenResponse>


    @Headers("CONTENT-TYPE:application/json", "Grant-type:password")
    @POST("/v1/token")
    fun login(@Body email: String, @Body password: String): Call<LoginResponse>
}