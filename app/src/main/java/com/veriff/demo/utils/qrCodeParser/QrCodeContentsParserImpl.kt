package com.veriff.demo.utils.qrCodeParser

import java.net.MalformedURLException
import java.net.URL

class QrCodeContentsParserImpl : QrCodeContentsParser {

    private var baseUrl: String? = null
    private var sessionToken: String? = null

    override fun parseQrCodeContents(contents: String): Pair<String?, String?> {
        val url: URL? = createUrl(contents)
        if (url != null) {
            setHostAndTokenFromUrl(url)
        } else {
            sessionToken = contents
        }

        return Pair(baseUrl, sessionToken)
    }

    private fun createUrl(contents: String): URL? {
        return try {
            URL(contents)
        } catch (e: MalformedURLException) {
            null
        }

    }

    private fun setHostAndTokenFromUrl(url: URL?) {
        url.let {
            baseUrl = url!!.protocol + "://" + url.host + "/"
            sessionToken = url.path.split("/".toRegex())[2]
        }
    }

}