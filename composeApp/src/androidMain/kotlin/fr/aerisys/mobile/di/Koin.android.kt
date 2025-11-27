package fr.aerisys.mobile.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import fr.aerisys.mobile.db.AerisysDatabase
import org.koin.core.module.Module
import org.koin.dsl.module


actual fun databaseModule() = module {
    single {
        //Penser à faire un Build -> "Compile all Sources" pour générer le MyDatabase
        val driver = AndroidSqliteDriver(AerisysDatabase.Schema, get(), "aerisys.db")
        AerisysDatabase(driver)
    }
}

