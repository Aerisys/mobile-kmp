package fr.aerisys.mobile.viewModel

import androidx.lifecycle.ViewModel
import fr.aerisys.mobile.db.AerisysDatabase

class MainViewModel(
    private val database: AerisysDatabase
) : ViewModel() {

    init {
        insertAndDisplayUser()
    }

    private fun insertAndDisplayUser() {
        val userQueries = database.usersQueries
        val dronesQueries = database.dronesQueries

        dronesQueries.deleteAllDrones()
        userQueries.deleteAllUsers()

       userQueries.insertUser(
           username = "John Doe",
           email = "john.doe@example.com",
            password = "password123"
        )

        val users = userQueries.selectAllUsers().executeAsList()
        println("Users in database: $users")
    }
}
