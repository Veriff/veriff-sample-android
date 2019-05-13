package com.veriff.demo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*

class AppStatics {


    companion object {
        @JvmStatic
        val URL_STAGING = "https://front3.staging.vrff.io/"

        @JvmStatic
        val TOKEN_RESULT = 101

        @JvmStatic
        val REQUEST_VERIFF = 8000

        @JvmStatic
        val REQUEST_SUCCESSFUL = 201

        @JvmStatic
        val GSON: Gson = GsonBuilder().registerTypeAdapter(Date::class.java, DateTypeAdapter()).create()
    }

}