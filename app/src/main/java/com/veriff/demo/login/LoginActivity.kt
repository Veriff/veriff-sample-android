package com.veriff.demo.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.veriff.demo.MainActivity
import com.veriff.demo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginMVP.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            startMainFlow()
        }


        txtSkipLogin.setOnClickListener {
            startMainFlow()
        }
    }

    override fun startMainFlow() {
        MainActivity.start(this)
        finish()
    }


}
