package fr.aerisys.mobile.model


data class Drone(
    val id: Long =0,
    val user_id: Long = 0,
    val name: String = "",
    val mac_adress: String = "null",
    val ip_adress: String = "null",
    val flight_mode: String = "null",
    val motor_power: Int = 0,
    val altitude_limit: Int = 0,
    val added_at: String = "null"
)