package fr.aerisys.mobile.model

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class KtorCameraStreamClient(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun readStream(ipAddress: String): Flow<ByteArray> = flow {
        val url = if (ipAddress.startsWith("http")) ipAddress else "http://$ipAddress"

        println("MJPEG: Connecting to camera stream at $url")

        val response = httpClient.get(url) {
            // Forcer la fermeture côté serveur pour éviter l'erreur "request body length should be specified"
            header("Connection", "close")
            header("Accept", "multipart/x-mixed-replace")
        }

        println("MJPEG: Connected, starting to read stream")


        val channel = response.bodyAsChannel()

        val boundary = response.headers["Content-Type"]?.substringAfter("boundary=") ?: "frame"
        val delimiter = ("--$boundary").encodeToByteArray()

        var buffer = ByteArray(0)
        val temp = ByteArray(4096)

        while (true) {
            val count = channel.readAvailable(temp)
            if (count < 0) break               // EOF
            if (count == 0) {
                if (channel.isClosedForRead) break
                delay(10)                      // pas de données pour l'instant, attendre un peu
                continue
            }

            buffer += temp.copyOf(count)

            while (true) {
                val index = buffer.indexOfSequence(delimiter)
                if (index == -1) break

                val frame = buffer.copyOfRange(0, index)
                if (frame.isNotEmpty()) emit(frame)

                buffer = buffer.copyOfRange(index + delimiter.size, buffer.size)
            }
        }
    }.flowOn(dispatcher)
}

private fun ByteArray.indexOfSequence(seq: ByteArray, from: Int = 0): Int {
    if (seq.isEmpty()) return from.coerceAtMost(this.size)
    val start = from.coerceAtLeast(0)
    val last = this.size - seq.size
    if (last < start) return -1
    var i = start
    while (i <= last) {
        var j = 0
        while (j < seq.size && this[i + j] == seq[j]) j++
        if (j == seq.size) return i
        i++
    }
    return -1
}

private fun ByteArray.startsWith(prefix: ByteArray): Boolean {
    if (this.size < prefix.size) return false
    for (i in prefix.indices) {
        if (this[i] != prefix[i]) return false
    }
    return true
}

private fun ByteArray.endsWith(suffix: ByteArray): Boolean {
    if (this.size < suffix.size) return false
    val offset = this.size - suffix.size
    for (i in suffix.indices) {
        if (this[offset + i] != suffix[i]) return false
    }
    return true
}