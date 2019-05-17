package com.veriff.demo.base

import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSourceI
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSourceI.Callback
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParserI

open class VeriffFlowModel(private val qrCodeContentsParser: QrCodeContentsParserI,
                           private val sessionTokenDataSource: SessionTokenDataSourceI) {

    fun parseQrCodeContents(contents: String): Pair<String?, String?> {
        return qrCodeContentsParser.parseQrCodeContents(contents)
    }

    fun getSessionToken(callback: Callback) {
        sessionTokenDataSource.getToken(callback)
    }

    fun getSessionTokenForUser(accessToken: String, callback: Callback) {
        sessionTokenDataSource.getTokenForUser(accessToken, callback)
    }

}