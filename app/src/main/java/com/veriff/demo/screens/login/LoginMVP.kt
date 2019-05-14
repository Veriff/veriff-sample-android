package com.veriff.demo.screens.login

import com.veriff.demo.base.VeriffFlowMVP

interface LoginMVP : VeriffFlowMVP {
    interface View : VeriffFlowMVP.View
    abstract class Presenter(view: View) : VeriffFlowMVP.Presenter(view)
}