package com.veriff.demo.service

import com.veriff.demo.data.TokenPayload
import com.veriff.demo.data.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenService {

    @Headers("X-AUTH-CLIENT:24d887f4-ad3e-43da-bc98-c8099ad6f430", "CONTENT-TYPE:application/json")
    @POST("/v1/sessions")
    fun getToken(@Header("X-SIGNATURE") signature: String, @Body payload: TokenPayload): Call<TokenResponse>
}