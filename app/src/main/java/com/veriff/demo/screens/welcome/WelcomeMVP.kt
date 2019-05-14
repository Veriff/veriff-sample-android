package com.veriff.demo.screens.welcome

import com.veriff.demo.base.VeriffFlowMVP
import com.veriff.demo.base.VeriffFlowModel

interface WelcomeMVP : VeriffFlowMVP {
    interface View : VeriffFlowMVP.View {
        fun setLoggedInView()
        fun setLoggedOutView()
    }

    abstract class Presenter(view: View, model: WelcomeModel) : VeriffFlowMVP.Presenter(view, model as VeriffFlowModel)
}