package fr.aerisys.mobile.model

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow

class KtorCameraClient(
    val httpClient: HttpClient,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun fetchCameras() {}
}