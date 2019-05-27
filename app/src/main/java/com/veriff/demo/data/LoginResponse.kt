package com.veriff.demo.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("token_type")
        val tokenType: String? = "",

        @SerializedName("access_token")
        val accessToken: String? = "",

        @SerializedName("refresh_token")
        val refreshToken: String? = "",

        @SerializedName("expires_in")
        val expiresIn: Long? = 0L
)