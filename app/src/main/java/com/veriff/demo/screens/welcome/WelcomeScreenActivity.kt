package com.veriff.demo.screens.welcome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.veriff.demo.AppStatics
import com.veriff.demo.R
import com.veriff.demo.SettingsActivity
import com.veriff.demo.dataSources.VeriffTokenDataSource
import com.veriff.demo.loging.Log
import com.veriff.demo.screens.login.LoginScreenActivity
import com.veriff.demo.service.TokenService
import com.veriff.demo.utils.GeneralUtils
import kotlinx.android.synthetic.main.activity_main.*
import mobi.lab.veriff.network.AcceptHeaderInterceptor
import mobi.lab.veriff.util.LangUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.MalformedURLException
import java.net.URL

class WelcomeScreenActivity : com.veriff.demo.base.BaseActivity(), WelcomeMVP.View {

    private var sessionToken: String? = null
    private var baseUrl = AppStatics.URL_STAGING

    private lateinit var presenter: WelcomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        launchButton.setOnClickListener {
            presenter.startVeriffFlow()
        }

        txtSignIn.setOnClickListener {
            LoginScreenActivity.start(this)
        }

        val tokenService = GeneralUtils.createRetrofit(log).create(TokenService::class.java)
        presenter = WelcomePresenter(this, VeriffTokenDataSource(tokenService = tokenService))
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbarMain)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarMain.setNavigationIcon(R.drawable.ic_settings)
        toolbarMain.setNavigationOnClickListener {
            startActivityForResult(SettingsActivity.createIntent(this), AppStatics.TOKEN_RESULT)
        }
    }

    override fun startVeriffFlow(sessionToken: String) {
        GeneralUtils.launchVeriffSDK(sessionToken, this@WelcomeScreenActivity, baseUrl)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppStatics.TOKEN_RESULT && resultCode == Activity.RESULT_OK) {
            val contents = SettingsActivity.readExtra(data!!)
            val parser = QrCodeContentsParser(contents)
            parser.parse()
            if (!LangUtils.isStringEmpty(sessionToken)) {
                GeneralUtils.launchVeriffSDK(sessionToken!!, this@WelcomeScreenActivity, baseUrl)
            } else {
                Toast.makeText(this@WelcomeScreenActivity, "No token available, try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private inner class QrCodeContentsParser internal constructor(internal var contents: String) {
        internal var url: URL? = null

        private val isUrl: Boolean
            get() = url != null

        init {
            this.url = createUrl(contents)
        }

        private fun createUrl(contents: String): URL? {
            try {
                return URL(contents)
            } catch (e: MalformedURLException) {
                return null
            }

        }

        internal fun parse() {
            if (isUrl) {
                // Demo interface QR codes contain host and session token
                setHostAndTokenFromUrl()
            } else {
                // Back office QR codes only contain session token
                sessionToken = contents
            }
        }

        private fun setHostAndTokenFromUrl() {
            baseUrl = url!!.protocol + "://" + url!!.host + "/"
            sessionToken = url!!.path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
        }
    }


    companion object {
        private val log = Log.getInstance("Retrofit")

        private fun createRetrofit(): Retrofit {
            val logInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> log.d(message) })
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient: OkHttpClient
            val retrofit: Retrofit

            // create regular retrofit client
            okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(AcceptHeaderInterceptor())
                    .addInterceptor(logInterceptor)
                    .build()

            retrofit = Retrofit.Builder()
                    .baseUrl(AppStatics.URL_STAGING)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(AppStatics.GSON))
                    .build()

            return retrofit
        }

        fun start(activity: Activity) {
            val starter = Intent(activity, WelcomeScreenActivity::class.java)
            activity.startActivity(starter)
        }
    }

}
