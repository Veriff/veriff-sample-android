package com.veriff.demo.screens.welcome

import com.veriff.demo.base.VeriffFlowMVP

interface WelcomeMVP : VeriffFlowMVP {
    interface View : VeriffFlowMVP.View {
        fun setLoggedInView()
        fun setLoggedOutView()
        fun navigateToLogin()
        fun showLogoutConfirmation()
    }
}