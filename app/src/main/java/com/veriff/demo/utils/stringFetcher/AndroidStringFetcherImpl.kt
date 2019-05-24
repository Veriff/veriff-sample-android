package com.veriff.demo.utils.stringFetcher

import android.content.Context
import android.support.annotation.StringRes

class AndroidStringFetcherImpl(private val context: Context) : StringFetcher {

    override fun getString(@StringRes id: Int): String {
        return context.resources.getString(id)
    }
}