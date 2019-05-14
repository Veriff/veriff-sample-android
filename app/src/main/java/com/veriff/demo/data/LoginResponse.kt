package com.veriff.demo.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("accessToken")
        val accessToken: String = "",

        @SerializedName("refreshToken")
        val refreshToken: String = "",

        @SerializedName("expiresIn")
        val expiresIn: Long = 0L
)