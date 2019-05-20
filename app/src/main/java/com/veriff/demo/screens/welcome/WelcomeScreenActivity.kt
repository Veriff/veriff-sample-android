package com.veriff.demo.screens.welcome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.widget.Toast
import com.veriff.demo.AppStatics
import com.veriff.demo.R
import com.veriff.demo.SettingsActivity
import com.veriff.demo.screens.login.LoginScreenActivity
import com.veriff.demo.utils.GeneralUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class WelcomeScreenActivity : com.veriff.demo.base.BaseActivity(), WelcomeMVP.View {

    private lateinit var presenter: WelcomeMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = get { parametersOf(this) }
        setupToolbar()

        btnLaunchVeriffFlow.setOnClickListener {
            presenter.startVeriffFlow()
        }

        txtSignIn.setOnClickListener {
            presenter.onSignInClicked()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbarMain)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarMain.setNavigationIcon(R.drawable.ic_settings)
        toolbarMain.setNavigationOnClickListener {
            startActivityForResult(SettingsActivity.createIntent(this), AppStatics.TOKEN_RESULT)
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
        GeneralUtils.launchVeriffSDK(sessionToken, this@WelcomeScreenActivity, url)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun setLoggedInView() {
        txtSignIn.text = resources.getString(R.string.sign_out)
    }

    override fun setLoggedOutView() {
        txtSignIn.text = resources.getString(R.string.sign_in)
    }


    override fun navigateToLogin() {
        LoginScreenActivity.start(this)
    }

    override fun showLogoutConfirmation() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, mobi.lab.veriff.R.style.vrffAlertDialogStyle))
        builder.setMessage(getString(R.string.demo_alert_logout))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.demo_yes)) { _, _ -> presenter.onLogoutConfirmed() }
                .setNegativeButton(getString(R.string.demo_no)) { _, _ -> presenter.onLogoutCancelled() }
        val dialog = builder.create()
        dialog.show()
    }

    override fun showProgress() {

    }

    override fun stopProgress() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppStatics.TOKEN_RESULT && resultCode == Activity.RESULT_OK) {
            val contents = SettingsActivity.readExtra(data!!)
            presenter.parseQrCodeContents(contents)
        }
    }

}
