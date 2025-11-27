package fr.aerisys.mobile.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import fr.aerisys.mobile.db.AerisysDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import okhttp3.ConnectionPool
import org.koin.dsl.module
import java.util.concurrent.TimeUnit
import kotlin.invoke


actual fun databaseModule() = module {
    single {
        //Penser à faire un Build -> "Compile all Sources" pour générer le MyDatabase
        val driver = AndroidSqliteDriver(AerisysDatabase.Schema, get(), "aerisys.db")
        AerisysDatabase(driver)
    }
}

actual val client = HttpClient(OkHttp) {
    install(Logging) {
        logger = object : Logger { override fun log(message: String) { println(message) } }
        level = LogLevel.INFO
    }

    install(HttpTimeout) {
        requestTimeoutMillis = Long.MAX_VALUE
        socketTimeoutMillis = Long.MAX_VALUE
        connectTimeoutMillis = 15_000L
    }

    engine {
        config {
            // OkHttp configuration adaptée au streaming MJPEG
            connectionPool(ConnectionPool(5, 5, TimeUnit.SECONDS))
            retryOnConnectionFailure(true)
        }
    }
}