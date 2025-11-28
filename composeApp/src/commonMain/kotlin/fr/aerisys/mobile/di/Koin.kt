package fr.aerisys.mobile.di

import fr.aerisys.mobile.model.KtorCameraStreamClient
import fr.aerisys.mobile.viewModel.CameraStreamViewModel
import fr.aerisys.mobile.viewModel.CameraViewModel
import fr.aerisys.mobile.viewModel.MainViewModel
import fr.aerisys.mobile.viewModel.UserViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun databaseModule(): Module

expect val client: HttpClient

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(apiModule, databaseModule(), viewModelModule)
    }

val apiModule = module {
    single {
        client
    }

    singleOf(::KtorCameraStreamClient)
}

val viewModelModule = module {
    factory { Dispatchers.IO }

    viewModelOf(::MainViewModel)
    single {
        CameraViewModel(get())
    }
    single {
        UserViewModel(get())
    }
    viewModelOf(::CameraStreamViewModel)
}