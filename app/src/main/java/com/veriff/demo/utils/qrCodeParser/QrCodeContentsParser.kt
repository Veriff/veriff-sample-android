package com.veriff.demo.utils.qrCodeParser

interface QrCodeContentsParser {
    fun parseQrCodeContents(contents: String): Pair<String?, String?>
}