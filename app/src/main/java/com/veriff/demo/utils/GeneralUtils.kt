package com.veriff.demo.utils

import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.veriff.demo.AppConfig
import com.veriff.demo.loging.Log
import mobi.lab.veriff.data.Veriff
import mobi.lab.veriff.util.LogAccess
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest

class GeneralUtils {


    companion object {
        @JvmStatic
        fun sha256(base: String): String {
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(base.toByteArray(charset("UTF-8")))
                val hexString = StringBuffer()

                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }

                return hexString.toString().toUpperCase()
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }

        }


        @JvmStatic
        fun launchVeriffSDK(sessionToken: String, activity: AppCompatActivity, baseUrl: String) {
            //enable logging for the library
            Veriff.setLoggingImplementation(Log.getInstance(activity))
            val veriffSDK = Veriff.Builder(baseUrl, sessionToken)
            veriffSDK.launch(activity, AppConfig.REQUEST_VERIFF)
        }


        @JvmStatic
        fun createRetrofit(log: LogAccess, gson: Gson): Retrofit {
            val logInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> log.d(message) })
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient: OkHttpClient
            val retrofit: Retrofit

            val acceptInterceptor = Interceptor { chain ->
                val HEADER_ACCEPT = "Accept"
                val HEADER_VALUE_ACCEPT = "application/vnd.veriff.v1+json"
                var request = chain.request()
                request = request.newBuilder()
                        .removeHeader(HEADER_ACCEPT)
                        .addHeader(HEADER_ACCEPT, HEADER_VALUE_ACCEPT)
                        .build()
                chain.proceed(request)
            }

            // create regular retrofit client
            okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(acceptInterceptor)
                    .addInterceptor(logInterceptor)
                    .build()

            retrofit = Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit
        }


        @JvmStatic
        fun getCurrMillis(): Long {
            return System.currentTimeMillis()
        }
    }


}