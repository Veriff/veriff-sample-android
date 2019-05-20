package com.veriff.demo.screens.welcome

import com.veriff.demo.base.VeriffFlowModel
import com.veriff.demo.data.dataSources.sessionToken.SessionTokenDataSourceI
import com.veriff.demo.utils.qrCodeParser.QrCodeContentsParserI

class WelcomeModel(qrCodeContentsParser: QrCodeContentsParserI,
                   sessionTokenDataSource: SessionTokenDataSourceI)
    : VeriffFlowModel(qrCodeContentsParser, sessionTokenDataSource)