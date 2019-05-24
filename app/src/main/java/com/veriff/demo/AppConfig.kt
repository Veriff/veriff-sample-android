package com.veriff.demo

class AppConfig {


    companion object {
        @JvmStatic
//        val URL_STAGING = "https://front3.staging.vrff.io/"
        val URL_STAGING = "https://front2.staging.vrff.io/"

        const val API_CLIENT_ID = "24d887f4-ad3e-43da-bc98-c8099ad6f430"

        @JvmStatic
        val TOKEN_RESULT = 101

        @JvmStatic
        val REQUEST_VERIFF = 8000

        @JvmStatic
        val REQUEST_SUCCESSFUL = 201


        val PREF_ACCESS_TOKEN = "pref_access_token"
        val PREF_REFRESH_TOKEN = "pref_refresh_token"
        val PREF_EXPIERES_IN = "pref_expieres_in"
        val PREF_LAST_LOGGED_IN = "pref_last_logged_in"
    }

}