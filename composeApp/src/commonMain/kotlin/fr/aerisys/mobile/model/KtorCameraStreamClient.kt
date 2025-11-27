package fr.aerisys.mobile.model

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class KtorCameraStreamClient(
    val httpClient: HttpClient,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun readStream(url: String): Flow<ByteArray> = flow {
        val httpResponse = httpClient.get(url)
        val channel = httpResponse.bodyAsChannel()

        val buffer = ByteArray(1024)
        var baos = ByteArray(0)

        while (!channel.isClosedForRead) {
            val n = channel.readAvailable(buffer, 0, buffer.size)
            if (n <= 0) continue

            val chunk = buffer.copyOf(n)
            val text = chunk.decodeToString()

            if (text.contains("boundary=") || text.contains("--frame")) {
                if (baos.isNotEmpty()) {
                    emit(baos)
                }
                baos = ByteArray(0)
            } else {
                baos += chunk
            }
        }
    }.flowOn(dispatcher)
}