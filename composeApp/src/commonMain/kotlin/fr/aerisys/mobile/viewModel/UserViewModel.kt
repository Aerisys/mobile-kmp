package fr.aerisys.mobile.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import fr.aerisys.mobile.db.AerisysDatabase
import fr.aerisys.mobile.utils.Hashing
import fraerisysmobile.db.Users


data class AccountState(
    val emailChecked: Boolean = false,
    val existingUser: Users? = null
)
class UserViewModel(
    private val database: AerisysDatabase
) : ViewModel() {
    private val _state = mutableStateOf(AccountState())
    val state: State<AccountState> = _state

    fun checkEmail(email: String) {
        val userQueries = database.usersQueries
        val user = userQueries.selectUserByEmail(email).executeAsOneOrNull()
        _state.value = _state.value.copy(
            existingUser = user,
            emailChecked = true
        )
    }

    fun createUser(email: String, password : String, username : String) : Users? {
        val userQueries = database.usersQueries
        val hashedPassword = Hashing.hashPassword(password)
        userQueries.insertUser(
            username = username,
            email = email,
            password = hashedPassword
        )
        return userQueries.selectUserByEmail(email).executeAsOneOrNull()
    }

    fun login(email: String, password: String): Users? {
        val userQueries = database.usersQueries
        val hashedPassword = Hashing.hashPassword(password)
        return userQueries.loginUserByEmailAndPassword(email, hashedPassword).executeAsOneOrNull()
    }
}