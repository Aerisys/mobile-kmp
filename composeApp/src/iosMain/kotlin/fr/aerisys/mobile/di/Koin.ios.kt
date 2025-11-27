package fr.aerisys.mobile.di

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
