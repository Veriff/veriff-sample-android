package com.veriff.demo.screens.welcome

import com.veriff.demo.base.VeriffFlowMVP

interface WelcomeMVP : VeriffFlowMVP {
    interface View : VeriffFlowMVP.View
    abstract class Presenter(view: View) : VeriffFlowMVP.Presenter(view)
}