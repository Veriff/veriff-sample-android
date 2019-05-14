package com.veriff.demo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veriff.demo.data.dataSources.login.UserDataSource
import com.veriff.demo.data.dataSources.login.UserDataSourceI
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSourceI
import com.veriff.demo.data.dataSources.sessionToken.VeriffSessionTokenDataSource
import com.veriff.demo.loging.Log
import com.veriff.demo.screens.login.LoginMVP
import com.veriff.demo.screens.login.LoginModel
import com.veriff.demo.screens.login.LoginPresenter
import com.veriff.demo.screens.welcome.WelcomeMVP
import com.veriff.demo.screens.welcome.WelcomeModel
import com.veriff.demo.screens.welcome.WelcomePresenter
import com.veriff.demo.service.AppNetworkService
import com.veriff.demo.utils.GeneralUtils
import com.veriff.demo.utils.localStorage.LocalStorageI
import com.veriff.demo.utils.localStorage.SharedPrefLocalStorage
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParser
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParserI
import com.veriff.demo.utils.stringFetcher.AndroidStringFetcher
import com.veriff.demo.utils.stringFetcher.StringFetcherI
import mobi.lab.veriff.util.LogAccess
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.*

class AppModule {

    companion object {
        private val appModule = module {
            single<LogAccess> { Log.getInstance(androidContext()) }
            single<Gson> {
                GsonBuilder()
                        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                        .create()
            }
            single<AppNetworkService> {
                GeneralUtils.createRetrofit(get(), gson = get()).create(AppNetworkService::class.java)
            }
            single<QrCodeContentsParserI> { QrCodeContentsParser() }
            single<SessionTokenDataSourceI> { VeriffSessionTokenDataSource(get(), gson = get()) }
            single<StringFetcherI> { AndroidStringFetcher(androidContext()) }
            single<LocalStorageI> {
                SharedPrefLocalStorage(androidContext(), "PREF_" + BuildConfig.APPLICATION_ID)
            }
            single<UserDataSourceI> {
                UserDataSource(appNetworkService = get(), localStorage = get())
            }
        }

        private val welcomeModule = module {
            factory {
                WelcomeModel(
                        sessionTokenDataSource = get(),
                        qrCodeContentsParser = get()
                )
            }
            factory<WelcomeMVP.Presenter> { (view: WelcomeMVP.View) ->
                WelcomePresenter(
                        model = get(),
                        view = view,
                        loginModel = get()
                )
            }
        }

        private val loginModule = module {
            factory {
                LoginModel(
                        qrCodeContentsParser = get(),
                        sessionTokenDataSource = get(),
                        userDataSource = get()
                )
            }
            factory<LoginMVP.Presenter> { (view: LoginMVP.View) ->
                LoginPresenter(
                        view = view,
                        model = get(),
                        stringFetcher = get(),
                        loginModel = get()
                )
            }
        }

        val modules = listOf(
                appModule,
                welcomeModule,
                loginModule
        )
    }

}