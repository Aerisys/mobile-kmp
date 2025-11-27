package fr.aerisys.mobile.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.posix.memcpy

actual object Hashing {
    @OptIn(ExperimentalForeignApi::class)
    actual fun hashPassword(password: String): String {
        val inputData = (password as NSString).dataUsingEncoding(NSUTF8StringEncoding)!!
        val length = CC_SHA256_DIGEST_LENGTH
        val outputData = UByteArray(length)

        memScoped {
            val hash = allocArray<UByteVar>(length)
            CC_SHA256(inputData.bytes, inputData.length.toUInt(), hash)
            outputData.usePinned {
                memcpy(it.addressOf(0), hash, length.toULong())
            }
        }

        return outputData.joinToString("") { it.toString(16).padStart(2, '0') }
    }
}
