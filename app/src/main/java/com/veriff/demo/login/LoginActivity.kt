package com.veriff.demo.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.veriff.demo.AppStatics.Companion.URL_STAGING
import com.veriff.demo.R
import com.veriff.demo.loging.Log
import com.veriff.demo.service.TokenService
import com.veriff.demo.utils.GeneralUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginMVP.View {

    private val log = Log.getInstance("LoginActivity")
    private var baseUrl = URL_STAGING

    private lateinit var presenter: LoginMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tokenService = GeneralUtils.createRetrofit(log).create(TokenService::class.java)
        presenter = LoginPresenter(this, log, tokenService = tokenService)

        launch_button.setOnClick {
            presenter.startVeriffFlow()
        }

    }

    override fun startVeriffFlow(sessionToken: String) {
        launch_button.stopLoading()
        GeneralUtils.launchVeriffSDK(sessionToken, this@LoginActivity, baseUrl)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
    }


}
