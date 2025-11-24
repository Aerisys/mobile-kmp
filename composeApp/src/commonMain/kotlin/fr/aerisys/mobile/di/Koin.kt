package fr.aerisys.mobile.di

import fr.aerisys.mobile.ui.viewmodel.DroneViewModel
import fr.aerisys.mobile.viewmodel.droneHome.MapDroneViewModel
import fr.aerisys.mobile.viewmodel.droneHome.GraphDroneHomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect fun databaseModule(): Module

val droneViewModelModule = module {
    factory<CoroutineDispatcher> { Dispatchers.IO } // Fournit le dispatcher
    viewModelOf(::DroneViewModel)
}

val graphDroneHomeViewModelModule = module {
    factory<CoroutineDispatcher> { Dispatchers.IO } // Fournit le dispatcher
    viewModelOf(::GraphDroneHomeViewModel)
}

val droneMapViewModel = module {
    viewModelOf(::MapDroneViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(databaseModule(), droneViewModelModule, graphDroneHomeViewModelModule, droneMapViewModel)
    }

fun initKoin() = initKoin {}
