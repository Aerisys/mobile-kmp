package fr.aerisys.mobile.model

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import fr.aerisys.mobile.db.AerisysDatabase
import org.koin.dsl.module

actual fun databaseModule() = module {
    single {
        val driver = AndroidSqliteDriver(AerisysDatabase.Schema, get(), "AerisysDatabase.db")
        AerisysDatabase(driver)
    }
}

