package com.veriff.demo.screens.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.veriff.demo.AppStatics.Companion.URL_STAGING
import com.veriff.demo.R
import com.veriff.demo.base.BaseActivity
import com.veriff.demo.dataSources.VeriffTokenDataSource
import com.veriff.demo.loging.Log
import com.veriff.demo.service.TokenService
import com.veriff.demo.utils.GeneralUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginScreenActivity : BaseActivity(), LoginMVP.View {

    private val log = Log.getInstance("LoginScreenActivity")
    private var baseUrl = URL_STAGING

    private lateinit var presenter: LoginMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar()

        btnLogin.setOnClick {
            presenter.startVeriffFlow()
        }


        val tokenService = GeneralUtils.createRetrofit(log).create(TokenService::class.java)
        presenter = LoginPresenter(this, tokenDataSource = VeriffTokenDataSource(tokenService))
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbarLogin)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarLogin.setNavigationIcon(R.drawable.ic_back_dark)
        toolbarLogin.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun startVeriffFlow(sessionToken: String) {
        btnLogin.stopLoading()
        GeneralUtils.launchVeriffSDK(sessionToken, this@LoginScreenActivity, baseUrl)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this@LoginScreenActivity, msg, Toast.LENGTH_LONG).show()
    }


    companion object {
        fun start(activity: BaseActivity) {
            activity.startActivity(Intent(activity, LoginScreenActivity::class.java))
        }
    }

}
