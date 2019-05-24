package com.veriff.demo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veriff.demo.data.dataSources.login.DummyUserDataSourceImpl
import com.veriff.demo.data.dataSources.login.UserDataSource
import com.veriff.demo.data.dataSources.login.UserDataSourceImpl
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSource
import com.veriff.demo.data.dataSources.sessionToken.VeriffSessionTokenDataSourceImpl
import com.veriff.demo.loging.Log
import com.veriff.demo.screens.login.LoginMVP
import com.veriff.demo.screens.login.LoginModel
import com.veriff.demo.screens.login.LoginPresenter
import com.veriff.demo.screens.welcome.WelcomeMVP
import com.veriff.demo.screens.welcome.WelcomeModel
import com.veriff.demo.screens.welcome.WelcomePresenter
import com.veriff.demo.service.AppNetworkService
import com.veriff.demo.utils.GeneralUtils
import com.veriff.demo.utils.localStorage.LocalStorage
import com.veriff.demo.utils.localStorage.SharedPrefLocalStorageImpl
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParser
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParserImpl
import com.veriff.demo.utils.stringFetcher.AndroidStringFetcherImpl
import com.veriff.demo.utils.stringFetcher.StringFetcher
import mobi.lab.veriff.util.LogAccess
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

class AppModule {

    companion object {
        private val appModule = module {
            single<LogAccess> { Log.getInstance(androidContext()) }
            single<Gson> {
                GsonBuilder()
                        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
                        .serializeNulls()
                        .create()
            }
            single<AppNetworkService> {
                GeneralUtils.createRetrofit(get(), gson = get()).create(AppNetworkService::class.java)
            }
            single<QrCodeContentsParser> { QrCodeContentsParserImpl() }
            single<SessionTokenDataSource> { VeriffSessionTokenDataSourceImpl(get(), gson = get()) }
            single<StringFetcher> { AndroidStringFetcherImpl(androidContext()) }
            single<LocalStorage> {
                SharedPrefLocalStorageImpl(androidContext(), "PREF_" + BuildConfig.APPLICATION_ID)
            }


            single<UserDataSource>(named("dummy")) { DummyUserDataSourceImpl() }
            single<UserDataSource>(named("network")) {
                UserDataSourceImpl(appNetworkService = get(), localStorage = get())
            }
        }

        private val welcomeModule = module {
            factory {
                WelcomeModel(
                        sessionTokenDataSource = get()
                )
            }
            factory<WelcomeMVP.Presenter> { (view: WelcomeMVP.View) ->
                WelcomePresenter(
                        model = get(),
                        view = view,
                        qrCodeContentsParser = get(),
                        loginModel = get(),
                        stringFetcher = get()
                )
            }
        }

        private val loginModule = module {
            factory {
                LoginModel(
                        sessionTokenDataSource = get(),
                        userDataSource = get(named("network"))
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