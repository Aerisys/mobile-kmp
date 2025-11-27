package fr.aerisys.mobile.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import fr.aerisys.mobile.db.AerisysDatabase
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
