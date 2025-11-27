package fr.aerisys.mobile.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import fr.aerisys.mobile.db.AerisysDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.config
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule() = module {
    single {
        val driver = NativeSqliteDriver(AerisysDatabase.Schema, "aerisys.db")
        AerisysDatabase(driver)
    }
}

fun doInitKoin(): Boolean {
    return try {
        initKoin {}
        true
    } catch (e: Throwable) {
        println("Koin init failed: ${e::class.simpleName}: ${e.message}")
        e.printStackTrace()
        false
    }
}

actual val client = HttpClient {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
        level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
    }
    install(HttpTimeout) {
        requestTimeoutMillis = Long.MAX_VALUE
        socketTimeoutMillis = Long.MAX_VALUE
    }
}