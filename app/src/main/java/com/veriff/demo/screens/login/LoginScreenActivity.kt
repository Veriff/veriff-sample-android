package com.veriff.demo.screens.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.veriff.demo.R
import com.veriff.demo.base.BaseActivity
import com.veriff.demo.utils.GeneralUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class LoginScreenActivity : BaseActivity(), LoginMVP.View {

    private lateinit var presenter: LoginMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = get { parametersOf(this) }
        setupToolbar()

        btnLogin.setOnClickListener {
            txt_email.error = null
            txt_password.error = null
            presenter.login(txt_email.text.toString(), txt_password.text.toString())
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbarLogin)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarLogin.setNavigationIcon(R.drawable.ic_back_dark)
        toolbarLogin.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun startVeriffFlow(sessionToken: String, url: String) {
        GeneralUtils.launchVeriffSDK(sessionToken, this@LoginScreenActivity, url)
    }

    override fun showEmailError(msg: String) {
        txt_email.error = msg
    }

    override fun showPasswordError(msg: String) {
        txt_password.error = msg
    }

    override fun showLoginError(msg: String) {
        showToast(msg)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this@LoginScreenActivity, msg, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {

    }

    override fun stopProgress() {
    }


    companion object {
        fun start(activity: BaseActivity) {
            activity.startActivity(Intent(activity, LoginScreenActivity::class.java))
        }
    }

}
