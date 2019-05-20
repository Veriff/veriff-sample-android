package com.veriff.demo.screens.login

import android.content.Intent
import android.os.Bundle
import android.view.View
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
            txtEmail.error = null
            txtPassword.error = null
            presenter.login(txtEmail.text.toString(), txtPassword.text.toString())
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

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    override fun startVeriffFlow(sessionToken: String, url: String) {
        GeneralUtils.launchVeriffSDK(sessionToken, this@LoginScreenActivity, url)
        finish()
    }

    override fun showEmailError(msg: String) {
        txtEmail.error = msg
    }

    override fun showPasswordError(msg: String) {
        txtPassword.error = msg
    }

    override fun showLoginError(msg: String) {
        showToast(msg)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this@LoginScreenActivity, msg, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        txtEmail.isEnabled = false
        txtPassword.isEnabled = false

        btnLogin.isEnabled = false
        progressButton.visibility = View.VISIBLE
        txtButton.visibility = View.GONE
    }

    override fun stopProgress() {
        txtEmail.isEnabled = true
        txtPassword.isEnabled = true

        btnLogin.isEnabled = true
        progressButton.visibility = View.GONE
        txtButton.visibility = View.VISIBLE
    }


    companion object {
        fun start(activity: BaseActivity) {
            activity.startActivity(Intent(activity, LoginScreenActivity::class.java))
        }
    }

}
