package com.veriff.demo.utils.qrCodeParser

interface QrCodeContentsParserI {
    fun parseQrCodeContents(contents: String): Pair<String?, String?>
}