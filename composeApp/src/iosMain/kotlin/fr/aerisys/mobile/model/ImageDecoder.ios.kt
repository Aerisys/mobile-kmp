package fr.aerisys.mobile.model

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun decodeImage(bytes: ByteArray): ImageBitmap? {
    return bytes.usePinned {
        val data = NSData.create(bytes = it.addressOf(0), length = bytes.size.toULong())
        val uiImage = UIImage.imageWithData(data) ?: return null
        uiImage.toImageBitmap()
    }
}

private fun UIImage.toImageBitmap(): ImageBitmap {
    TODO("Not yet implemented")
}
