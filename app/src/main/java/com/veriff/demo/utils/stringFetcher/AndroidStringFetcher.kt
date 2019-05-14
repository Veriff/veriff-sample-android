package com.veriff.demo.utils.stringFetcher

import android.content.Context
import android.support.annotation.StringRes

class AndroidStringFetcher(private val context: Context) : StringFetcherI {


    override fun getString(@StringRes id: Int): String {
        return context.resources.getString(id)
    }
}